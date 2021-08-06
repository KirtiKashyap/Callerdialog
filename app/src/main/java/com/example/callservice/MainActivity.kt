package com.example.callservice

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.callservice.receiver.Restarter
import com.example.callservice.service.YourService


class MainActivity : AppCompatActivity() {
    var mServiceIntent: Intent? = null
    private var mYourService: YourService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pass touch events to the background activity
//        window.addFlags(
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                    or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//        )

        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            )!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE
            )!= PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            )!= PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SYSTEM_ALERT_WINDOW
            )!= PackageManager.PERMISSION_GRANTED|| ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.PROCESS_OUTGOING_CALLS
            )!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
                ),
                1
            )
        }


        mYourService = YourService()
        mServiceIntent = Intent(this, mYourService!!.javaClass)
        if (!isMyServiceRunning(mYourService!!.javaClass)) {
            startService(mServiceIntent)
        }
    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }
    override fun onDestroy() {
        //stopService(mServiceIntent);
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }
}