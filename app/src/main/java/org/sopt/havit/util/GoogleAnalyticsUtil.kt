package org.sopt.havit.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import org.sopt.havit.BuildConfig.IS_PROD

object GoogleAnalyticsUtil {

    // property
    private const val STATUS_INSTALLED = "installed"
    private const val ANALYTICS_USER_PROP = "app_type"

    // parameter
    private const val CONTENT_CHECK = "click_content_check"
    private const val SCREEN_TIME = "screen_time"
    private const val SITE_NUM = "site_num"
    private const val ICON_NUM = "icon_num"

    // save contents
    const val SELECT_CATEGORY = "select_category"
    const val CONTENT_CUSTOM = "content_custom"
    const val CLICK_COMPLETE_SAVE_CONTENT = "click_complete_save_content"
    const val SETTING_ALARM = "setting_alarm"
    const val CLICK_COMPLETE_SETTING_ALARM = "click_complete_setting_alarm"
    const val MODIFY_TITLE = "modify_title"
    const val CLICK_COMPLETE_MODIFY_TITLE = "click_complete_modify_title"

    // create category
    const val ADD_CATEGORY = "add_category"
    const val SELECT_ICON = "select_icon"
    const val CLICK_COMPLETE_SELECT_ICON = "click_complete_select_icon"

    const val CLICK_MY_CATEGORY = "click_my_category"
    const val CLICK_MY_CONTENT = "click_my_content"
    const val CLICK_CHECKED_CONTENT = "click_checked_content"
    const val CLICK_GO_BACK = "click_go_back"
    const val CLICK_REFRESH = "click_refresh"
    const val CLICK_SHARE = "click_share"
    const val CLICK_CONTENT_CHECK = "click_content_check"
    const val CONTENT_SCREEN_TIME = "content_screen_time"

    // 홈
    const val CLICK_MUST_SEE_CONTENT = "click_must_see_content"
    const val CLICK_SEARCH_CONTENT = "click_search_content"
    const val CLICK_WHOLE_CATEGORY = "click_whole_category"
    const val CLICK_HAVIT_SERVICE_GUIDE = "click_havit_service_guide"
    const val CLICK_SEE_MORE = "click_see_more"
    const val CLICK_RECOMMENDED_SITE = "click_recommended_site"
    const val GNB_HOME = "gnb_home"
    const val GNB_CATEGORY = "gnb_category"
    const val GNB_MYPAGE = "gnb_mypage"
    const val GNB_ADD_CONTENT = "gnb_add_content"

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
                putInt(SITE_NUM, siteNum)
            }
            Firebase.analytics.logEvent(contentName, params)
        }
    }

    fun logClickEventOnAddCategory(iconNum: Int) {
        val params = Bundle().apply {
            putInt(ICON_NUM, iconNum)
        }
        Firebase.analytics.logEvent(CLICK_COMPLETE_SELECT_ICON, params)
    }
}
