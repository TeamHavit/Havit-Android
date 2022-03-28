package org.sopt.havit.ui.notification

import android.content.Context
import androidx.lifecycle.ViewModel
import org.sopt.havit.util.MySharedPreference

class NotificationViewModel(context: Context): ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)
}