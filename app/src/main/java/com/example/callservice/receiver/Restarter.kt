package com.example.callservice.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.example.callservice.service.YourService

import java.util.*

class Restarter : BroadcastReceiver() {
    private val callStartTime: Date? = null
    private var savedNumber: String? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Broadcast Listened", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context!!.startForegroundService(Intent(context, YourService::class.java))
        } else {
            context!!.startService(Intent(context, YourService::class.java))
        }


        try {
            if (intent!!.action == "android.intent.action.NEW_OUTGOING_CALL") {
                savedNumber =
                    intent!!.extras!!.getString("android.intent.extra.PHONE_NUMBER")
            } else {
                val stateStr = intent!!.extras!!.getString(TelephonyManager.EXTRA_STATE)
                val number = intent!!.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                var state = 0
                if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                    state = TelephonyManager.CALL_STATE_IDLE
                } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    state = TelephonyManager.CALL_STATE_OFFHOOK
                    if (number != null && !number.isEmpty() && number != "null") {
                        Log.d("TEST :", "NUMBER =>$number")
                        Toast.makeText(context, "OutGoing Call$number", Toast.LENGTH_LONG).show()
                        val outgoingIntent = Intent(context, MyCustomDialog::class.java)
                        outgoingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        outgoingIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        outgoingIntent.putExtra("phone_no", "Outgoing Call: $number")
                        Handler().postDelayed({ context!!.startActivity(outgoingIntent) }, 1000)
                    }
                } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                    state = TelephonyManager.CALL_STATE_RINGING
                    if (number != null && !number.isEmpty() && number != "null") {
                        Log.d("TEST :", "NUMBER =>$number")
                        Toast.makeText(context, "OutGoing Call$number", Toast.LENGTH_LONG).show()
                        val incomingIntent = Intent(context, MyCustomDialog::class.java)
                        incomingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        incomingIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        incomingIntent.putExtra("phone_no", "Incoming Call From: $number")
                        Handler().postDelayed({ context!!.startActivity(incomingIntent) }, 1000)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}