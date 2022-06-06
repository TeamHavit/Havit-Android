package org.sopt.havit.ui.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.util.MySharedPreference

class NotificationViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    // TODO: 나중에 알림 모아보기 서버 나오면 그걸로 바꿀 것
    // 현재는 최근저장콘텐츠 api 호출
    private val _contentsList = MutableLiveData<List<ContentsSimpleResponse.ContentsSimpleData>>()
    val contentsList: LiveData<List<ContentsSimpleResponse.ContentsSimpleData>> = _contentsList
    fun requestContentsTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getContentsRecent()
                _contentsList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }
}