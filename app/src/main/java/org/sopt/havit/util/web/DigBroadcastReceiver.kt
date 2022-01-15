package org.sopt.havit.util.web

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.provider.UserDictionary
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RemoteViews
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.auth.Session.getCurrentSession
import com.kakao.sdk.template.model.Content
import org.koin.android.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.ui.contents.ContentsViewModel
import org.sopt.havit.ui.contents.WebViewModel
import org.sopt.havit.ui.mypage.MyPageFragment
import org.sopt.havit.ui.mypage.MyPageViewModel
import org.sopt.havit.util.MySharedPreference
import java.lang.ref.WeakReference




class DigBroadcastReceiver:BroadcastReceiver() {



    //var isHavit :LiveData<Boolean> = _isHavit

    var _isHavit = MutableLiveData<Boolean>()
    companion object{

        fun createRemoteViews(
            context: Context,
            havit: Boolean,
            flagAdded: Boolean
        ): RemoteViews {
            val remoteViews = RemoteViews(context.packageName, R.layout.view_custom_web)
            /*val digIcon: Int = if (digAdded) R.drawable.ic_home_black_24dp else R.drawable.ic_dashboard_black_24dp
            remoteViews.setImageViewResource(R.id.havit, digIcon)
            val flagIcon: Int = if (flagAdded) R.drawable.ic_home_black_24dp else R.drawable.ic_dashboard_black_24dp
            remoteViews.setImageViewResource(R.id.flag_action, flagIcon)*/

            if(havit){
                remoteViews.setImageViewResource(R.id.iv_webview_unread,R.drawable.ic_contents_webread_2)
            }else{
                remoteViews.setImageViewResource(R.id.iv_webview_unread,R.drawable.ic_contents_webunread)
            }

            return remoteViews
        }


        val clickableIDs = intArrayOf(R.id.ll_webview)


        /**
         * @return The PendingIntent that will be triggered when the user clicks on the Views listed by
         */

        fun getPendingIntent(context: Context): PendingIntent {
            val digIntent = Intent(context, DigBroadcastReceiver()::class.java)
            return PendingIntent.getBroadcast(context, 3, digIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }


    }
    override fun onReceive(context: Context, intent: Intent) {
        _isHavit.value=MySharedPreference.getHavit(context)
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

            if (clickedID == R.id.ll_webview) {
                Toast.makeText(context,"해빗하였습니다.",Toast.LENGTH_SHORT).show()

                //createRemoteViews(context,false,false)
                /*if(_isHavit.value == true){
                    _isHavit.postValue(false)
                    MySharedPreference.setHavit(context,false)
                    var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var view2 :View = inflater.inflate(R.layout.view_custom_web,null)
                    var img = view2.findViewById<ImageView>(R.id.iv_webview_unread)
                    img.setImageResource(R.drawable.ic_contents_webunread)

                    val session: CustomTabsSession = SessionHelper.getCurrentSession ?: return
                    session.setSecondaryToolbarViews(
                        createRemoteViews(context, _isHavit.value!!, false),
                        clickableIDs,
                        getPendingIntent(context)
                    )

                }else{
                    _isHavit.postValue(true)
                    MySharedPreference.setHavit(context,true)
                    var inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    var view2 :View = inflater.inflate(R.layout.view_custom_web,null)
                    var img = view2.findViewById<ImageView>(R.id.iv_webview_unread)
                    img.setImageResource(R.drawable.ic_contents_webread_2)

                    val session: CustomTabsSession = SessionHelper.getCurrentSession ?: return
                    session.setSecondaryToolbarViews(
                        createRemoteViews(context, _isHavit.value!!, false),
                        clickableIDs,
                        getPendingIntent(context)
                    )


                }*/



            }

        }





    }

    inner class SessionHelper {
        private var sCurrentSession: WeakReference<CustomTabsSession>? = null

        /**
         * @return The current [CustomTabsSession] object.
         */

        fun getCurrentSession(): CustomTabsSession? {
            return sCurrentSession?.get()
        }

        /**
         * Sets the current session to the given one.
         * @param session The current session.
         */
        fun setCurrentSession(session: CustomTabsSession?) {
            sCurrentSession = WeakReference<CustomTabsSession>(session)
        }
    }




}