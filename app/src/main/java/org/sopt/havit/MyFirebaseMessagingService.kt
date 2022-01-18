package org.sopt.havit

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.sopt.havit.MainActivity
import org.sopt.havit.R

const val channelID = "notification_channel"
const val channelName = "org.sopt.androidsharing"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // 토큰 변경될 떄
        Log.d("MyFirebaseMessagingService", "Refreshed token: $token")
    }

    private fun getDeviceToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TokenTest", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("TokenTest", token)

        })
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyFirebaseMessagingService", "onMessageReceived")
        getDeviceToken()

        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService_data", "Message data payload: ${remoteMessage.data}")
            val dataFromServer = remoteMessage.data
            val title = dataFromServer["title"]
            val description = dataFromServer["body"]
            val image = dataFromServer["image"]
            val url = dataFromServer["url"]

            if (title != null && description != null){
                generateNotification(title, description)
            }
        } else {
            Log.d("MyFirebaseMessagingService[data]", "Empty Data")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MyFirebaseMessagingService_notification", it.title!!)
            generateNotification(it.title!!, it.body!!)
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("org.sopt.havit", R.layout.push_notification)
        remoteView.setTextViewText(R.id.tv_title, title)
        remoteView.setTextViewText(R.id.tv_description, message)
        remoteView.setImageViewResource(R.id.iv_image, R.drawable.ic_havit_logo)

        Log.d(TAG, "getRemoteView")

        return remoteView
    }

    private fun generateNotification(title: String, message: String) {
        Log.d("MyFirebaseMessagingService", "generateNotification")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 500, 1000, 500))  // 1초 울리고 0.5초 쉬고
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
//            .setContentTitle(title)
//            .setContentText(message)

        builder = builder.setContent(getRemoteView(title, message))    // custom

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /** Oreo Version 이하일때 처리 하는 코드 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "under Oreo Version")
            val notificationChannel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }

    companion object {
        const val TAG = "MyFirebaseMessagingService"
    }

}
