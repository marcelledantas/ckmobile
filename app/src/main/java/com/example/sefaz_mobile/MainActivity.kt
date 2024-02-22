package com.example.sefaz_mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    private lateinit var mobileNode: MainCKMobileNode
    private lateinit var message: String;
    private lateinit var messageType: String;
    private lateinit var destination: String;

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val textTitle: TextView = findViewById(R.id.textView1)
        val editTextUserInput: EditText = findViewById(R.id.editTextUserInput)
        val editTextUserMessageInput: EditText = findViewById(R.id.editTextUserInput1)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)

        val spinner: Spinner = findViewById(R.id.message_type)
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.message_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected item from the Spinner

                when (parent?.getItemAtPosition(position).toString()) {
                    "Mensagem unicast" -> {
                        editTextUserInput.hint = "Digite UUID do nó móvel"
                        messageType = "unicast"
                    }
                    "Mensagem groupcast" -> {
                        editTextUserInput.hint = "Digite o número do grupo"
                        messageType = "groupcast"
                    }
                    else -> {
                        editTextUserInput.hint = ""
                        messageType = ""
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected if needed
            }
        }

        buttonSubmit.setOnClickListener {
            val userInput = editTextUserInput.text.toString()

            val gfgThread = Thread {
                try {

                    val appContext = applicationContext
                    if (appContext != null) {
                        mobileNode = MainCKMobileNode(this@MainActivity)


                        // Initialize mobileNode and perform network operations
                        mobileNode.fazTudo(editTextUserMessageInput.text.toString(),messageType,editTextUserInput.text.toString())

                        // Your network activity code comes here
                    } else {
                        // Log an error or handle the case where applicationContext is null
                        Log.e("MainActivity", "Application context is null")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            gfgThread.start()

        }

   }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine scope when the activity is destroyed
        cancel()
    }
}

