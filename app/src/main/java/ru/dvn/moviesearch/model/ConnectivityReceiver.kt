package ru.dvn.moviesearch.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import java.lang.StringBuilder

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("Network Message")
            append(intent.action)
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}