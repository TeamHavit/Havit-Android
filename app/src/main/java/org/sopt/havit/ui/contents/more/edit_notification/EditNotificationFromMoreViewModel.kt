package org.sopt.havit.ui.contents.more.edit_notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.data.remote.ModifyContentNotificationParams
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.util.CalenderUtil
import org.sopt.havit.util.Event
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditNotificationFromMoreViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val havitApi: HavitApi
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
    private val finalIndex: LiveData<Int?>
        get() = _finalIndex

    fun syncTempDataWithFinalData() {
        _tempNotificationTime.value = finalNotificationTime.value
        _tempIndex.value = finalIndex.value
    }

    private fun syncFinalDataWithTempData() {
        _finalNotificationTime.value = tempNotificationTime.value
        _finalIndex.value = tempIndex.value
    }

    private fun resetFinalData() {
        _finalIndex.value = null
        _finalNotificationTime.value = null
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
        viewModelScope.launch {
            kotlin.runCatching {
                havitApi.deleteContentNotification(
                    requireNotNull(contentId.value)
                )
            }.onSuccess {
                resetFinalData()
                userClicksOnButton(SUCCESS)
            }.onFailure {
                userClicksOnButton(FAIL)
            }
        }

    }

    fun patchNotification() {
        viewModelScope.launch {
            val time = tempNotificationTime.value?.substring(0, 16)?.replace(".", "-")
            kotlin.runCatching {
                havitApi.modifyContentNotification(
                    requireNotNull(contentId.value),
                    ModifyContentNotificationParams(requireNotNull(time))
                )
            }.onSuccess {
                syncFinalDataWithTempData()
                userClicksOnButton(SUCCESS)
            }.onFailure {
                if (isCode500(it.message))
                    userClicksOnButton(REQUEST_DELETE)
                else userClicksOnButton(FAIL)
            }
        }
    }

    private fun isCode500(errorMessage: String?): Boolean {
        return errorMessage?.substringAfter(" ")?.trim()?.toIntOrNull() == 500
    }

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton(string: String) {
        _isNetworkCorrespondenceEnd.value = Event(string)
    }

    companion object {
        const val SUCCESS = "SUCCESS"
        const val FAIL = "FAIL"
        const val REQUEST_DELETE = "REQUEST_DELETE"
    }
}
