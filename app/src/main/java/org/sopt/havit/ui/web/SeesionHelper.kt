package org.sopt.havit.ui.web

import androidx.annotation.Nullable
import androidx.browser.customtabs.CustomTabsSession
import java.lang.ref.WeakReference

object SessionHelper {
    private var sCurrentSession: WeakReference<CustomTabsSession>? = null

    /**
     * @return The current [CustomTabsSession] object.
     */
    @get:Nullable
    val getCurrentSession: CustomTabsSession?
        get() = if (sCurrentSession == null) null else sCurrentSession!!.get()

    /**
     * Sets the current session to the given one.
     * @param session The current session.
     */
    fun setCurrentSession(session: CustomTabsSession?) {
        if (session != null) {
            sCurrentSession = WeakReference(session)
        }
    }


}