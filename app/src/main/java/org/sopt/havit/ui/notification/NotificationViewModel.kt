package org.sopt.havit.ui.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.util.MySharedPreference

class NotificationViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _contentsList = MutableLiveData<List<NotificationResponse.NotificationData>>()
    val contentsList: LiveData<List<NotificationResponse.NotificationData>> = _contentsList

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
}