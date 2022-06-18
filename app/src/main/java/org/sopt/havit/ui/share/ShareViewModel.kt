package org.sopt.havit.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    val token = authRepository.getAccessToken()

    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()

    private var _notificationTIme = MutableLiveData<String>()
    val notificationTime get() = _notificationTIme

    private var _isTimeDirectlySetFromUser = MutableLiveData<Boolean>()
    val isTimeDirectlySetFromUser get() = _isTimeDirectlySetFromUser

    fun setNotificationTime(time: String) {
        _notificationTIme.value = time
    }

    fun isTimeDirectlySetFromUser(boolean: Boolean) {
        _isTimeDirectlySetFromUser.value = boolean
    }

    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }

    fun isTitleModified() = originTitle.value != currTitle.value
}
