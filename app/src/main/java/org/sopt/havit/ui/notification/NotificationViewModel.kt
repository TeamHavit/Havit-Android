package org.sopt.havit.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.NotificationResponse.NotificationData
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val havitApi: HavitApi
) : ViewModel() {

    private val _contentsList = MutableLiveData<List<NotificationData>>()
    val contentsList: LiveData<List<NotificationData>> = _contentsList
    private val _contentLoadState = MutableLiveData(true)
    val contentLoadState: LiveData<Boolean> = _contentLoadState

    fun requestContentsTaken(option: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _contentLoadState.postValue(true)
                val response =
                    havitApi.getNotification(option)
                _contentsList.postValue(response.data)
                _contentLoadState.postValue(false)
            } catch (e: Exception) {
            }
        }
    }

    private val _isSeen = MutableLiveData<Boolean>()
    val isSeen: LiveData<Boolean> = _isSeen
    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch {
            try {
                val response =
                    havitApi.isHavit(ContentsHavitRequest(contentsId))
                _isSeen.postValue(response.data.isSeen)
            } catch (e: Exception) {
            }
        }
    }

    fun updateContentsList(list: List<NotificationData>) {
        _contentsList.value = list
    }

    // 콘텐츠 삭제를 서버에게 요청하는 코드
    fun deleteContents(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                havitApi.deleteContents(contentsId)
            } catch (e: Exception) {
            }
        }
    }
}
