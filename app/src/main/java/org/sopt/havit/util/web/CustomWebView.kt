package org.sopt.havit.util.web

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.ui.contents.ContentsViewModel
import org.sopt.havit.util.MySharedPreference

object CustomWebView {

    private var package_name = "com.android.chrome"

    fun setView(context: Context, url: String) {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()

        // to set the toolbar color use CustomTabColorSchemeParams
        // since CustomTabsIntent.Builder().setToolBarColor() is deprecated


        params.setToolbarColor(ContextCompat.getColor(context, R.color.white))
        builder.setDefaultColorSchemeParams(params.build())

        // shows the title of web-page in toolbar
        builder.setShowTitle(true)
        builder.setUrlBarHidingEnabled(false)


        // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        val intent = Intent(Intent(context, MainActivity::class.java))


        //val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_home_black_24dp)
        val pendingFlagIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
/*
        val menuItemPendingIntent: PendingIntent = createPendingIntent(context,
            ActionBroadcastReceiver.ACTION_MENU_ITEM
        )*/
        //builder.addMenuItem("해빗하기", menuItemPendingIntent)
        val customBuilder = builder.build()
        if (context.isPackageInstalled(package_name)) {
            // if chrome is available use chrome custom tabs
            customBuilder.intent.setPackage(package_name)
            prepareBottombar(context, builder)
            customBuilder.launchUrl(
                context,
                Uri.parse("http://www.google.com/search?q=site%3Aokky.kr+%EA%B2%8C%EC%9D%B4%EB%B0%8D%EB%85%B8%ED%8A%B8%EB%B6%81&sxsrf=AOaemvJmwUdivp1tT-BKwO3cASw7GJNT-A%3A1641141349241&ei=ZdTRYbWADpb3hwPagJfQDA&oq=site%3Aokky.kr+%EA%B2%8C%EC%9D%B4%EB%B0%8D%EB%85%B8%ED%8A%B8%EB%B6%81&gs_lcp=ChNtb2JpbGUtZ3dzLXdpei1zZXJwEANKBAhBGABQx48LWIW-C2DiwAtoKXAAeAOAAbACiAH2BpIBBzUuMy4xLjGYAQCgAQHAAQE&sclient=mobile-gws-wiz-serp")
            )


        } else {
            // if not available use WebView to launch the url
        }

    }



    private fun prepareBottombar(context: Context, builder: CustomTabsIntent.Builder) {
        builder.setSecondaryToolbarViews(
            DigBroadcastReceiver.createRemoteViews(context, false, false),
            DigBroadcastReceiver.clickableIDs,
            DigBroadcastReceiver.getPendingIntent(context)
        )


    }



    private fun createPendingIntent(context: Context, actionSourceId: Int): PendingIntent {
        val actionIntent = Intent(
            context.applicationContext, ActionBroadcastReceiver::class.java
        )
        actionIntent.putExtra(ActionBroadcastReceiver.KEY_ACTION_SOURCE, actionSourceId)
        return PendingIntent.getBroadcast(
            context.applicationContext, actionSourceId, actionIntent, 0
        )
    }

    fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}