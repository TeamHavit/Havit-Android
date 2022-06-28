package org.sopt.havit.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.util.Event
import java.util.*
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
    private var _finalNotificationTime = MutableLiveData<String?>()
    val finalNotificationTime: LiveData<String?>
        get() = _finalNotificationTime

    private var _tempNotificationTime = MutableLiveData<String?>()
    val tempNotificationTime: LiveData<String?>
        get() = _tempNotificationTime

    private var _tempIndex = MutableLiveData<Int>()
    val tempIndex: LiveData<Int>
        get() = _tempIndex

    private var _finalIndex = MutableLiveData<Int>()
    val finalIndex: LiveData<Int>
        get() = _finalIndex

    fun syncTempDataWithFinalData() {
        _tempNotificationTime.value = finalNotificationTime.value
        _tempIndex.value = finalIndex.value
    }

    fun setNotificationTimeDirectly(time: String?) {
        _finalNotificationTime.value = time
        _tempIndex.value = 4
    }

    fun setNotificationTimeIndirectly(afterTime: AfterTime) {
        val cal = Calendar.getInstance().apply { time = Date() }
        cal.add(afterTime.type, afterTime.interval)
//        _notificationTime.value = cal.time
        _tempIndex.value = 4
    }

    fun setSelectedIndex(index: Int? = null, afterTime: AfterTime? = null) {
        _tempIndex.value = index ?: afterTime?.buttonIndex
        if (afterTime != null) setNotificationTimeIndirectly(afterTime)
    }

    private var _isTimeDirectlySetFromUser = MutableLiveData<Boolean>()
    val isTimeDirectlySetFromUser: LiveData<Boolean>
        get() = _isTimeDirectlySetFromUser

    fun isTimeDirectlySetFromUser(selectedIndex: Int?) {
        if (selectedIndex == null) throw IllegalStateException("Not Initialized Yet")
        _isTimeDirectlySetFromUser.value = selectedIndex == 4
    }

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }
}
