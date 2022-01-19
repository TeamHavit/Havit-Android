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
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.sopt.havit.ui.web.WebActivity

const val channelID = "notification_channel"
const val channelName = "org.sopt.androidsharing"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // 토큰 변경될 때
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

    // 1. push 알림 들어옴
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MyFirebaseMessagingService", "onMessageReceived")
        getDeviceToken()

        // 2-1. data 확인
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService_data", "Message data payload: ${remoteMessage.data}")
            val dataFromServer = remoteMessage.data
            val title = dataFromServer["title"]
            val description = dataFromServer["body"]
            val image = dataFromServer["image"]
            val url = dataFromServer["url"]

            generateNotification(title, description, image, url)
        }

        // 2-2. notification 확인
//        remoteMessage.notification?.let {
//            Log.d("MyFirebaseMessagingService_notification", it.title!!)
//            generateNotification(it.title!!, it.body!!, )
//        }
    }

    private fun isExistImage(url: String?): Boolean {
        if (url?.isEmpty() == true)
            return false
        return true
    }

    private fun getRemoteView(title: String?, message: String?, image: String?): RemoteViews {
        val remoteView = RemoteViews("org.sopt.havit", R.layout.push_notification)
        remoteView.setTextViewText(R.id.tv_title, title)
        remoteView.setTextViewText(R.id.tv_description, message)
        remoteView.setImageViewResource(R.id.iv_image, R.drawable.ic_havit_radious_10)

        Log.d(TAG, "getRemoteView")

        return remoteView
    }

    private fun generateNotification(
        title: String?,
        message: String?,
        image: String?,
        url: String?
    ) {
        Log.d("MyFirebaseMessagingService", "generateNotification")

        val intent = Intent(this, WebActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("url", url)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_havit_radious_10)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 500, 1000, 500))  // 1초 울리고 0.5초 쉬고
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message, image))    // custom

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
