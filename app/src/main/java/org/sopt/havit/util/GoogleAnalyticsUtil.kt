package org.sopt.havit.util

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import org.sopt.havit.BuildConfig.IS_DEV

object GoogleAnalyticsUtil {

    private const val STATUS_INSTALLED = "installed"
    private const val STATUS_INSTANT = "instant"
    private const val ANALYTICS_USER_PROP = "app_type"


    private const val CONTENT_CHECK = "CONTENT_CHECK"
    private const val SCREEN_TIME = "SCREEN_TIME"

    const val CLICK_MY_CATEGORY = "CLICK_MY_CATEGORY"
    const val CLICK_MY_CONTENT = "CLICK_MY_CONTENT"
    const val CLICK_CHECKED_CONTENT = "CLICK_CHECKED_CONTENT"
    const val CLICK_GO_BACK = "CLICK_GO_BACK"
    const val CLICK_REFRESH = "CLICK_REFRESH"
    const val CLICK_SHARE = "CLICK_SHARE"
    const val CLICK_CONTENT_CHECK = "CLICK_CONTENT_CHECK"
    const val CONTENT_SCREEN_TIME = "CONTENT_SCREEN_TIME"

    fun logScreenEvent(screenName: String) {
        if (IS_DEV) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            }
        } else {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            }
        }
    }

    fun logClickEvent(contentName: String) {
        if (IS_DEV) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
            Firebase.analytics.logEvent(contentName, null)
        } else {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(contentName, null)
        }
    }

    fun logClickEventWithContentCheck(contentName: String, contentCheck: Boolean) {
        if (IS_DEV) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
            Firebase.analytics.logEvent(contentName) {
                param(CONTENT_CHECK, contentCheck.toString())
            }
        } else {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(contentName) {
                param(CONTENT_CHECK, contentCheck.toString())
            }
        }
    }

    fun logScreenDurationTimeEvent(contentName: String, durationTime: Long) {
        if (IS_DEV) {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTANT)
            Firebase.analytics.logEvent(contentName) {
                param(SCREEN_TIME, durationTime)
            }
        } else {
            Firebase.analytics.setUserProperty(ANALYTICS_USER_PROP, STATUS_INSTALLED)
            Firebase.analytics.logEvent(contentName) {
                param(SCREEN_TIME, durationTime)
            }
        }
    }

}
