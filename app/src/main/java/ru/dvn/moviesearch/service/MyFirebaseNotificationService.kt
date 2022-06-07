package ru.dvn.moviesearch.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.dvn.moviesearch.R

class MyFirebaseNotificationService : FirebaseMessagingService() {
    companion object {
        private const val PUSH_TITLE_KEY = "title"
        private const val PUSH_CONTENT_KEY = "content"
        private const val CHANNEL_ID = "fcm_channel"
        private const val NOTIFICATION_ID = 101
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val messageData = message.data
        if (messageData.isNotEmpty()) {
            handleMessage(messageData)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun handleMessage(data: Map<String, String>) {
        val title = data[PUSH_TITLE_KEY]?.trim()
        val content = data[PUSH_CONTENT_KEY]?.trim()

        if (!title.isNullOrBlank() && !content.isNullOrBlank()) {
            showNotification(title, content)
        }
    }

    private fun showNotification(title: String, content: String) {
        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_notifications)
                setContentTitle(title)
                setContentText(content)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "FCM_CHANNEL"
        val description = "FCM_CHANNEL_DESCRIPTION"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
        }

        notificationManager.createNotificationChannel(channel)
    }

}