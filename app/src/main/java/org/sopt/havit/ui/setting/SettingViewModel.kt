package org.sopt.havit.ui.setting

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.util.MySharedPreference

class SettingViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _user = MutableLiveData<UserResponse.UserData>()
    val user: LiveData<UserResponse.UserData> = _user
    private val _version = MutableLiveData<String>()
    val version: LiveData<String> = _version

    private val _email = MutableLiveData<String>()  // 임시변수
    val email: LiveData<String> = _email

    // version 정보 가져옴
    fun setVersion(appVersion: String) {
        _version.postValue(appVersion)
    }

    // user data 가져옴
    fun requestUserInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.provideHavitApi(token)
                    .getUserData()
                _user.postValue(response.data)
                _email.postValue("babotaejun@kakao.com")    // 임시로 넣음
            } catch (e: Exception) {
            }
        }
    }
}