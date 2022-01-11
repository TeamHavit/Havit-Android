package org.sopt.havit.util.web

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import org.sopt.havit.R

class DigBroadcastReceiver:BroadcastReceiver() {

    companion object{

        fun createRemoteViews(
            context: Context,
            digAdded: Boolean,
            flagAdded: Boolean
        ): RemoteViews {
            val remoteViews = RemoteViews(context.packageName, R.layout.view_custom_web)
            /*val digIcon: Int = if (digAdded) R.drawable.ic_home_black_24dp else R.drawable.ic_dashboard_black_24dp
            remoteViews.setImageViewResource(R.id.havit, digIcon)
            val flagIcon: Int = if (flagAdded) R.drawable.ic_home_black_24dp else R.drawable.ic_dashboard_black_24dp
            remoteViews.setImageViewResource(R.id.flag_action, flagIcon)*/

            return remoteViews
        }

        val clickableIDs = intArrayOf(R.id.havit)


        /**
         * @return The PendingIntent that will be triggered when the user clicks on the Views listed by
         */

        fun getPendingIntent(context: Context): PendingIntent {
            val digIntent = Intent(context, DigBroadcastReceiver()::class.java)
            return PendingIntent.getBroadcast(context, 3, digIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
    override fun onReceive(context: Context, intent: Intent) {
        val uri: Uri? = intent.data
        if (uri != null) {

            Log.d("Broadcast URL",uri.toString())
            var toast = Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT)
            val view = toast.view
            view?.background?.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN)
            val text = view?.findViewById(android.R.id.message) as TextView
            text.setTextColor(ContextCompat.getColor(context, R.color.black))
            text.textAlignment = View.TEXT_ALIGNMENT_CENTER
            toast.setGravity(Gravity.BOTTOM,0,200)
            toast.show()

            val clickedID = intent.extras?.getInt(CustomTabsIntent.EXTRA_REMOTEVIEWS_CLICKED_ID)

            if (clickedID == R.id.havit) {
                Toast.makeText(context,"해빗하였습니다.",Toast.LENGTH_SHORT).show()

            }
        }
    }
}