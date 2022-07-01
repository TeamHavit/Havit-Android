package org.sopt.havit.ui.contents.more.edit_notification

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.util.CalenderUtil
import org.sopt.havit.util.Event
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditNotificationFromMoreViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    /** token */
    val token = authRepository.getAccessToken()

    /** notification time */
    private val contentId = MutableLiveData<Int>()

    fun initProperty(contentsMoreData: ContentsMoreData) {
        contentId.value = contentsMoreData.id
        _finalIsNotificationSet.value = contentsMoreData.isNotified
        _finalNotificationTime.value = contentsMoreData.notificationTime
        if (contentsMoreData.isNotified) _finalIndex.value = 4
    }

    fun isNotificationSet(): Boolean = finalNotificationTime.value != ""

    private var _finalIsNotificationSet = MutableLiveData<Boolean?>()
    val finalIsNotificationSet: LiveData<Boolean?>
        get() = _finalIsNotificationSet

    private var _finalNotificationTime = MutableLiveData<String?>()
    private val finalNotificationTime: LiveData<String?>
        get() = _finalNotificationTime

    private var _tempNotificationTime = MutableLiveData<String?>()
    val tempNotificationTime: LiveData<String?>
        get() = _tempNotificationTime

    private var _tempIndex = MutableLiveData<Int?>()
    val tempIndex: LiveData<Int?>
        get() = _tempIndex

    private var _finalIndex = MutableLiveData<Int?>()
    val finalIndex: LiveData<Int?>
        get() = _finalIndex

    fun syncTempDataWithFinalData() {
        _tempNotificationTime.value = finalNotificationTime.value
        _tempIndex.value = finalIndex.value
    }

    fun syncFinalDataWithTempData() {
        _finalNotificationTime.value = tempNotificationTime.value
        _finalIndex.value = tempIndex.value
    }

    fun setNotificationTimeDirectly(time: String?) {
        _tempNotificationTime.value = time
        _tempIndex.value = 4
    }

    fun setNotificationTimeIndirectly(afterTime: AfterTime) {
        val cal = Calendar.getInstance().apply { time = Date() }
        cal.add(requireNotNull(afterTime.type), requireNotNull(afterTime.interval))
        _tempNotificationTime.value = CalenderUtil.dateAndTimeWithDotFormatMD.format(cal.time)
        _tempIndex.value = afterTime.buttonIndex
    }

    fun setSelectedIndex(index: Int? = null, afterTime: AfterTime? = null) {
        _tempIndex.value = index ?: afterTime?.buttonIndex
    }

    fun isNotificationDataChanged(): Boolean {
        return if (tempIndex.value == 4 && finalIndex.value == 4)
            tempNotificationTime.value != finalNotificationTime.value
        else tempIndex.value != finalIndex.value
    }

    fun deleteNotification() {
        _finalIndex.value = null
        _finalNotificationTime.value = null
    }

    fun patchNotification() {
        viewModelScope.launch {
            kotlin.runCatching {
                val time = tempNotificationTime.value?.substring(0, 16)?.replace(".", "-")
                // TODO 알림수정 api 아직 안나옴
            }.onSuccess {
                Log.d(TAG, "patchNotification: onSuccess")
            }.onFailure {
                Log.d(TAG, "patchNotification: onFailure $it")
            }.run { userClicksOnButton() }
        }
    }

    // TODO 알림삭제 api 아직 안나옴

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }
}
