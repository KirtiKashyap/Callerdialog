package com.example.callservice.receiver

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.callservice.R
import kotlin.system.exitProcess

class MyCustomDialog : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setFinishOnTouchOutside(false)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_my_custom_dialog)
            setFinishOnTouchOutside(false)
            val collingText = findViewById<TextView>(R.id.callText)
            collingText.text = intent.extras!!.getString("phone_no")
           val  btnOk = findViewById<Button>(R.id.btnOk)
            btnOk.setOnClickListener{
                finish()
                exitProcess(0)
            }
        } catch (e: Exception) {
            Log.d("Exception", e.toString())
            e.printStackTrace()
        }
    }
}