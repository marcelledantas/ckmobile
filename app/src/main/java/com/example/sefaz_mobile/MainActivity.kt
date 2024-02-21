package com.example.sefaz_mobile

import PingUtil.ping
import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


class MainActivity : Activity(), CoroutineScope by MainScope() {
    private lateinit var mobileNode: MainCKMobileNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread {
            val isReachable = ping("192.168.1.8")
            Log.e("msg",isReachable.toString())
            println(isReachable)
        }.start()


        val gfgThread = Thread {
            try {

                // Call getProperties on the UI thread

                val appContext = applicationContext
                if (appContext != null) {
                    mobileNode = MainCKMobileNode(this@MainActivity)


                    // Initialize mobileNode and perform network operations
                    mobileNode.fazTudo()

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

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the coroutine scope when the activity is destroyed
        cancel()
    }
}


