package org.sopt.havit.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import org.sopt.havit.BuildConfig.IS_PROD

object GoogleAnalyticsUtil {

    private const val STATUS_INSTALLED = "installed"
    private const val STATUS_INSTANT = "instant"
    private const val ANALYTICS_USER_PROP = "app_type"


    private const val CONTENT_CHECK = "CONTENT_CHECK"
    private const val SCREEN_TIME = "SCREEN_TIME"
    private const val SITE_NUM = "SITE_NUM"

    const val CLICK_MY_CATEGORY = "CLICK_MY_CATEGORY"
    const val CLICK_MY_CONTENT = "CLICK_MY_CONTENT"
    const val CLICK_CHECKED_CONTENT = "CLICK_CHECKED_CONTENT"
    const val CLICK_GO_BACK = "CLICK_GO_BACK"
    const val CLICK_REFRESH = "CLICK_REFRESH"
    const val CLICK_SHARE = "CLICK_SHARE"
    const val CLICK_CONTENT_CHECK = "CLICK_CONTENT_CHECK"
    const val CONTENT_SCREEN_TIME = "CONTENT_SCREEN_TIME"

    // 홈
    const val CLICK_MUST_SEE_CONTENT = "CLICK_MUST_SEE_CONTENT"
    const val CLICK_SEARCH_CONTENT = "CLICK_SEARCH_CONTENT"
    const val CLICK_WHOLE_CATEGORY = "CLICK_WHOLE_CATEGORY"
    const val CLICK_HAVIT_SERVICE_GUIDE = "CLICK_HAVIT_SERVICE_GUIDE"
    const val CLICK_SEE_MORE = "CLICK_SEE_MORE"
    const val CLICK_RECOMMENDED_SITE = "CLICK_RECOMMENDED_SITE"
    const val GNB_HOME = "GNB_HOME"
    const val GNB_CATEGORY = "GNB_CATEGORY"
    const val GNB_MYPAGE = "GNB_MYPAGE"
    const val GNB_ADD_CONTENT = "GNB_ADD_CONTENT"

    fun logScreenEvent(screenName: String) {
        if (IS_PROD) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            }
        }
    }

    fun logClickEvent(contentName: String) {
        if (IS_PROD) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(contentName, null)
        }
    }

    fun logClickEventWithContentCheck(contentName: String, contentCheck: Boolean) {
        if (IS_PROD) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            val params = Bundle().apply {
                putBoolean(CONTENT_CHECK, contentCheck)
            }
            Firebase.analytics.logEvent(contentName, params)
        }
    }

    fun logScreenDurationTimeEvent(contentName: String, durationTime: Long) {
        if (IS_PROD) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(contentName) {
                param(SCREEN_TIME, durationTime)
            }
        }
    }

    fun logClickEventWithRecommendedSiteNum(contentName: String, siteNum: Int) {
        if (IS_PROD) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            val params = Bundle().apply {
                putString(SITE_NUM, siteNum.toString())
            }
            Firebase.analytics.logEvent(contentName, params)
        }
    }
}
