package org.sopt.havit.ui.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.NotificationResponse.NotificationData
import org.sopt.havit.util.MySharedPreference

class NotificationViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _contentsList = MutableLiveData<List<NotificationData>>()
    val contentsList: LiveData<List<NotificationData>> = _contentsList

    fun requestContentsTaken(option: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getNotification(option)
                _contentsList.postValue(response.data)
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
                    RetrofitObject.provideHavitApi(token)
                        .isHavit(ContentsHavitRequest(contentsId))
                _isSeen.postValue(response.data.isSeen)
            } catch (e: Exception) {
            }
        }
    }

    fun updateContentsList(list: List<NotificationData>) {
        _contentsList.value = list
    }
}
