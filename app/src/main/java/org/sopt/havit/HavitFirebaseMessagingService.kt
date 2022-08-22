package org.sopt.havit

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.ui.web.WebActivity
import javax.inject.Inject

const val channelID = "notification_channel"
const val channelName = "org.sopt.androidsharing"

@AndroidEntryPoint
class HavitFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var firebaseTokenManager: FirebaseTokenManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        firebaseTokenManager.sendRegistrationToServer(token)
    }

    // 1. push 알림 들어옴
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 2-1. data 확인
        if (remoteMessage.data.isNotEmpty()) {
            val dataFromServer = remoteMessage.data
            val title = dataFromServer["title"]
            val description = dataFromServer["body"]
            val image = dataFromServer["image"]
            val url = dataFromServer["url"]
            generateNotification(title, description, image, url)
        }

        // 2-2. notification 확인
        remoteMessage.notification?.let { generateNotification(it.title!!, it.body!!) }
    }

    private fun isExistImage(url: String?): Boolean {
        if (url?.isEmpty() == true)
            return false
        return true
    }

    private fun generateNotification(
        title: String?,
        message: String?,
        image: String? = null,
        url: String? = null
    ) {
        Log.d("MyFirebaseMessagingService", "$title // $message")
        val requestCode = System.currentTimeMillis().toInt()

        val intent = Intent(this, WebActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("url", url)
        val pendingIntent =
            PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_havit_radious_10)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 500, 1000, 500)) // 1초 울리고 0.5초 쉬고
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(requestCode, builder.build())
    }

    companion object {
        fun getDeviceToken() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.d("TokenTest", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    Log.d("TokenTest", token)
                }
            )
        }

        const val TAG = "MyFirebaseMessagingService"
    }
}