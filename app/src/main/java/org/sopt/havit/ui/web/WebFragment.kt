package org.sopt.havit.ui.web

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.*
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentWebBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.web.SessionHelper.setCurrentSession
import org.sopt.havit.util.MySharedPreference

class WebFragment : BaseBindingFragment<FragmentWebBinding>(R.layout.fragment_web) {


    lateinit var serviceConnection: CustomTabsServiceConnection
    lateinit var client: CustomTabsClient
    lateinit var session: CustomTabsSession
    var builder = CustomTabsIntent.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                mClient: CustomTabsClient
            ) {
                Log.d("Service", "Connected")
                client = mClient
                client.warmup(0L)
                val callback = RabbitCallback()
                session = client.newSession(RabbitCallback())!!
                setCurrentSession(session)
                builder.setSession(session)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("Service", "Disconnected")
            }
        }
        CustomTabsClient.bindCustomTabsService(
            requireContext(),
            "com.android.chrome",
            serviceConnection
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            val url =
                "https://github.com/boostcampwm-2021/android03-Contact/wiki/Git-%EC%82%AC%EC%9A%A9%EB%B2%95"
            //val url = "https://www.wikipedia.org"
            val customTabsIntent: CustomTabsIntent = builder.build()
            prepareBottombar(requireContext(), builder)

            customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))

    }


    private fun prepareBottombar(context: Context, builder: CustomTabsIntent.Builder) {
        builder.setSecondaryToolbarViews(
            HavitBroadcastReceiver.createRemoteViews(context, false),
            HavitBroadcastReceiver.clickableIDs,
            HavitBroadcastReceiver.getPendingIntent(context)
        )
        MySharedPreference.setHavit(context, false)


    }

    override fun onStart() {
        super.onStart()
        CustomTabsClient.bindCustomTabsService(
            requireContext(),
            "com.android.chrome",
            serviceConnection
        )
    }

    class RabbitCallback : CustomTabsCallback() {
        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
            super.onNavigationEvent(navigationEvent, extras)
            Log.d("Nav", navigationEvent.toString())
            when (navigationEvent) {
                1 -> Log.d("Navigation", "Start") // NAVIGATION_STARTED
                2 -> Log.d("Navigation", "Finished") // NAVIGATION_FINISHED
                3 -> Log.d("Navigation", "Failed") // NAVIGATION_FAILED
                4 -> Log.d("Navigation", "Aborted") // NAVIGATION_ABORTED
                5 -> Log.d("Navigation", "Tab Shown") // TAB_SHOWN
                6 -> Log.d("Navigation", "Tab Hidden") // TAB_HIDDEN
                else -> Log.d("Navigation", "Else")
            }
        }
    }


}