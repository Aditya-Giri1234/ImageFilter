package com.aditya.imagefilter.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.time.Duration

object Helper {

    private var toast:Toast?=null

    fun customToast(context: Context, msg: CharSequence, duration: Int) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(context, msg, duration)
        toast?.show()
    }

    fun customLog(tag:String , msg:String){
        Log.e(tag  , msg)
    }
     fun perFormBackPressed(activity: Activity) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                (activity as AppCompatActivity).onBackPressedDispatcher.addCallback(activity, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        activity.finish()
                    }
                })
            }

            else -> {
                activity.onBackPressed()
            }
        }
    }
}