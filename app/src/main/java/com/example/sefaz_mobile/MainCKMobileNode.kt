package com.example.sefaz_mobile

import android.app.Activity
import android.content.res.AssetManager
import android.widget.Toast
import ckafka.data.SwapData
//import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lac.cnclib.net.NodeConnection
import lac.cnclib.sddl.message.ApplicationMessage
import lac.cnclib.sddl.message.Message
import main.java.ckafka.mobile.CKMobileNode
import main.java.ckafka.mobile.tasks.SendLocationTask
import java.io.InputStream
import java.lang.reflect.Field
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.Properties
import java.util.UUID
import java.util.concurrent.TimeUnit

class MainCKMobileNode(private val activity: Activity) : CKMobileNode() {
    private var stepNumber = 0
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val assetManager: AssetManager = activity.assets

    fun fazTudo(message: String, typeMessage: String, sender: String) {
        coroutineScope.launch {
            if(typeMessage == "groupcast"){
                sendGroupcastMessage(sender, message)
            }
            else if(typeMessage == "unicast"){
                sendUnicastMessage(sender, message)
            }
        }
    }


    private fun sendMessageToPN(messageText: String) {
        val messageBytes: ByteArray = messageText.toByteArray(Charsets.UTF_8)
        val message: ApplicationMessage = createDefaultApplicationMessage()
        val data = SwapData()
        data.setMessage(messageBytes)
        data.setTopic("AppModel")
        message.setContentObject(data)
        sendMessageToGateway(message)
    }

    private fun sendUnicastMessage(uuid: String, messageText: String) {
        println(
            """
                    Mensagem unicast. Entre com o UUID do indivíduo:
                    HHHHHHHH-HHHH-HHHH-HHHH-HHHHHHHHHHHH
                    """.trimIndent()
        )
        println(String.format("Enviando mensagem |%s| para o indivíduo %s.", messageText, uuid))

        val privateData = SwapData()
        privateData.setMessage(messageText.toByteArray(Charsets.UTF_8))
        privateData.setTopic("PrivateMessageTopic")
        privateData.setRecipient(uuid)
        val message: ApplicationMessage = createDefaultApplicationMessage()
        message.setContentObject(privateData)
        sendMessageToGateway(message)
    }

    private fun sendGroupcastMessage(group: String, messageText: String) {
        val groupData = SwapData()
        groupData.setMessage(messageText.toByteArray(Charsets.UTF_8))
        groupData.setTopic("GroupMessageTopic")
        groupData.setRecipient(group)
        val message: ApplicationMessage = createDefaultApplicationMessage()
        message.setContentObject(groupData)
        sendMessageToGateway(message)
    }


    override fun getProperties() {
        try {
            // Convert strings to the desired types
            setPrivateField("gatewayIP", "192.168.1.8")
            setPrivateField("gatewayPort", 5500)

            val mnIDString = "01111111-1111-1111-1111-511111111111"
            val mnID = UUID.fromString(mnIDString)
            setPrivateField("mnID", mnID)

        } catch (e: Exception) {
            logger.error("Error loading properties file", e)
        }
    }

    private fun setPrivateField(fieldName: String, value: Any) {
        try {
            val field: Field = CKMobileNode::class.java.getDeclaredField(fieldName)
            field.isAccessible = true
            field.set(this, value)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }


    override fun connected(nodeConnection: NodeConnection?) {
        try {
            logger.debug("Connected")
            val sendlocationtask = SendLocationTask(this)
            scheduledFutureLocationTask = threadPool.scheduleWithFixedDelay(
                sendlocationtask,
                5000,
                60000,
                TimeUnit.MILLISECONDS
            )
        } catch (e: java.lang.Exception) {
            logger.error("Error scheduling SendLocationTask", e)
        }
    }

    override fun newMessageReceived(nodeConnection: NodeConnection?, message: Message) {
        try {
            val swp: SwapData = fromMessageToSwapData(message)
            if (swp.getTopic().equals("Ping")) {
                message.setSenderID(this.mnID)
                sendMessageToGateway(message)
            } else {
                val str = String(swp.getMessage(), StandardCharsets.UTF_8)
                showToast(String.format("Message received from %s: %s", message.getRecipientID(), str))
            }
        } catch (e: Exception) {
            logger.error("Error reading new message received")
        }
    }

    override fun disconnected(nodeConnection: NodeConnection?) {}

    override fun unsentMessages(nodeConnection: NodeConnection?, list: List<Message?>?) {}

    override fun internalException(nodeConnection: NodeConnection?, e: Exception?) {}

    private fun showToast(message: String) {
        // Use withContext(Dispatchers.Main) if you need to update UI elements from a coroutine
        coroutineScope.launch(Dispatchers.Main) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
