package org.sopt.havit.ui.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.MyFirebaseMessagingService.Companion.TAG
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.util.MySharedPreference
import java.security.KeyStore

class SettingViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _user = MutableLiveData<UserResponse.UserData>()
    val user: LiveData<UserResponse.UserData> = _user
    private val _version = MutableLiveData<String>()
    val version: LiveData<String> = _version
    private val _isLatest = MutableLiveData<Boolean>()
    val isLatest: LiveData<Boolean> = _isLatest

    // version 정보 가져옴
    fun setVersion(appVersion: String) {
        _version.postValue(appVersion)
        if (appVersion == "1.0")
            _isLatest.postValue(true)
        else
            _isLatest.postValue(false)
    }

    // user data 가져옴
    fun requestUserInfo() {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.provideHavitApi(token)
                    .getUserData()
                _user.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }
}