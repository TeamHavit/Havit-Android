package org.sopt.havit.ui.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.data.local.SettingPreference
import javax.inject.Inject

@HiltViewModel
class SettingAlarmViewModel @Inject constructor(
    private val pref: SettingPreference
) : ViewModel() {

    var isContentsNotiActivated = MutableLiveData(pref.isContentsNotiActivated)
    var isTotalNotiActivated = MutableLiveData(pref.isTotalNoticeActivated)

    fun updateContentsNotiPreference() {
        pref.isContentsNotiActivated = isContentsNotiActivated.value ?: true
    }

    fun setTotalNotiActivated() {
        pref.isTotalNoticeActivated = isTotalNotiActivated.value ?: true
    }
}
