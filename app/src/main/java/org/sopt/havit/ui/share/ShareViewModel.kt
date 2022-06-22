package org.sopt.havit.ui.share

import androidx.lifecycle.LiveData
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
    /** token */
    val token = authRepository.getAccessToken()

    /** title */
    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()
    fun isTitleModified() = originTitle.value != currTitle.value

    /** notification time */

    private var _notificationTime = MutableLiveData<String?>()
    val notificationTime: LiveData<String?>
        get() = _notificationTime

    fun setNotificationTime(time: String?) {
        _notificationTime.value = time
    }

    private var _isTimeDirectlySetFromUser = MutableLiveData<Boolean>()
    val isTimeDirectlySetFromUser: LiveData<Boolean>
        get() = _isTimeDirectlySetFromUser

    fun isTimeDirectlySetFromUser(boolean: Boolean) {
        _isTimeDirectlySetFromUser.value = boolean
    }

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }
}
