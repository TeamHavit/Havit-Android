package org.sopt.havit.ui.setting.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.BuildConfig
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.NewNicknameRequest
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.entity.Notice
import org.sopt.havit.domain.entity.VersionState
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    application: Application,
    private val havitApi: HavitApi
) : AndroidViewModel(application) {
    private val token = authRepository.getAccessToken()
    private val _user = MutableLiveData<UserResponse.UserData>()
    val user: LiveData<UserResponse.UserData> = _user
    val currentVersion: String = BuildConfig.VERSION_NAME
    private val _versionState = MutableLiveData(VersionState.Unknown)
    val versionState: LiveData<VersionState> = _versionState

    // 환경설정_내정보수정
    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> = _nickname

    fun getLatestVersion() {
        val context = getApplication<Application>().applicationContext
        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                _versionState.postValue(VersionState.Update)
            } else {
                _versionState.postValue(VersionState.Latest)
            }
        }.addOnFailureListener { exception ->
            _versionState.postValue(VersionState.Unknown)
            Log.e(TAG, "error getting update info(Play Store가 설치된 앱에서 사용해주세요)", exception)
        }
    }

    // user data 가져옴
    fun requestUserInfo() {
        viewModelScope.launch {
            try {
                val response = havitApi.getUserData()
                _user.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }

    // 프로필수정뷰 - editText에 닉네임 초기화
    fun setNickname(nickname: String) {
        _nickname.value = nickname
    }

    // 닉네임에 공백이 포함되지 않으면서, 0자 이상
    private val _isNicknameAvailable = MutableLiveData(true)
    val isNicknameAvailable: LiveData<Boolean> = _isNicknameAvailable
    fun isNicknameValid() {
        val isNicknameNotBlank: Boolean = nickname.value?.isNotBlank() ?: false
        val isNicknameHasWhiteSpace: Boolean = isNicknameHasWhiteSpace.value ?: false
        _isNicknameAvailable.value = isNicknameNotBlank && !isNicknameHasWhiteSpace
    }

    // 프로필수정뷰 - 닉네임 수정
    fun fetchNickname() {
        viewModelScope.launch {
            kotlin.runCatching {
                val newNickname = nickname.value?.trim()
                    ?: throw IllegalArgumentException("nickname cannot be null")
                havitApi.modifyUserNickname(NewNicknameRequest(newNickname))
            }.onSuccess {
                Log.d(TAG, "requestNewNickname: success")
            }.onFailure {
                Log.d(TAG, "requestNewNickname: fail $it")
            }
        }
    }


    private val _isNicknameHasWhiteSpace = MutableLiveData(false)
    val isNicknameHasWhiteSpace: LiveData<Boolean> = _isNicknameHasWhiteSpace
    fun isNickNameContainWhiteSpace() {
        _isNicknameHasWhiteSpace.value = nickname.value?.contains("\\s".toRegex()) ?: false
    }

    fun removeHavitAuthToken() {
        authRepository.removeHavitAuthToken()
    }

    fun unregister() {
        viewModelScope.launch {
            try {
                havitApi.deleteUser()
            } catch (e: Exception) {
                Log.d("SETTING", "error : $e")
            }
        }
    }

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>> = _noticeList

    fun getNoticeList() {
        viewModelScope.launch {
            kotlin.runCatching {
                havitApi.getNoticeList().data
            }.onSuccess {
                _noticeList.value = it
            }.onFailure {
                Log.d(TAG, "getNoticeList: fail $it")
            }
        }
    }
}
