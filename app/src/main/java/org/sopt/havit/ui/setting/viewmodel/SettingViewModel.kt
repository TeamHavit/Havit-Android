package org.sopt.havit.ui.setting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.NewNicknameRequest
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
//    private val token = MySharedPreference.getXAuthToken(context)

    private val token = authRepository.getAccessToken()

    private val _user = MutableLiveData<UserResponse.UserData>()
    val user: LiveData<UserResponse.UserData> = _user
    private val _version = MutableLiveData<String>()
    val version: LiveData<String> = _version
    private val _isLatest = MutableLiveData<Boolean>()
    val isLatest: LiveData<Boolean> = _isLatest

    // 환경설정_내정보수정
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    // version 정보 가져옴
    fun setVersion(appVersion: String) {
        _version.postValue(appVersion)
        Log.d("TOKEN", "setting token : $token")
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

    // 프로필수정뷰 - editText에 닉네임 초기화
    fun setNickname(nickname: String) {
        _nickname.postValue(nickname)
    }

    // 프로필수정뷰 - 닉네임 수정
    fun requestNewNickname(newNickname: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitObject.provideHavitApi(token).modifyUserNickname(
                    NewNicknameRequest(newNickname)
                )
                Log.d("Request New Nickname", "requestNewNickname: response : $response")
            } catch (e: Exception) {
                Log.d("Request New Nickname", "error : $e")
            }
        }
    }

    fun removeHavitAuthToken() {
        authRepository.removeHavitAuthToken()
    }

    fun unregister() {
        viewModelScope.launch {
            try {
                RetrofitObject.provideHavitApi(token).deleteUser()
            } catch (e: Exception) {
                Log.d("SETTING", "error : $e")
            }
        }
    }
}
