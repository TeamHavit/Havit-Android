package org.sopt.havit.ui.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.data.remote.SignUpRequest
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :
    ViewModel() {

    private var _isMoveToNextOrBack = MutableLiveData<Event<Boolean>>()
    var isMoveToNextOrBack: LiveData<Event<Boolean>> = _isMoveToNextOrBack

    var isNextClick = MutableLiveData(false)

    private var _isAlreadyUser = MutableLiveData<Event<SignInResponse>>()
    var isAlreadyUser: LiveData<Event<SignInResponse>> = _isAlreadyUser

    private var _isNeedScopes = MutableLiveData<Event<Boolean>>()
    var isNeedScopes: LiveData<Event<Boolean>> = _isNeedScopes

    private var _isReadyUser = MutableLiveData<Event<Boolean>>()
    var isReadyUser: LiveData<Event<Boolean>> = _isReadyUser

    private var _accessToken = MutableLiveData<String>()
    var accessToken: LiveData<String> = _accessToken

    private val _fcmToken = MutableLiveData<String>()

    private val _kakaoToken = MutableLiveData<String>()

    var nickName = MutableLiveData<String>()

    private var _havitUser = MutableLiveData<SignUpRequest>()

    var isAllCheck = MutableLiveData(false)
    var isTosUseCheck = MutableLiveData(false)
    var isTosInfoCheck = MutableLiveData(false)
    var isTosEventCheck = MutableLiveData(false)


    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.d("TAG", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.d("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            setKakaoToken(token.accessToken)
            setFcmToken()
            setNeedScopes()
        }
    }

    fun setNeedScopes() {
        _isNeedScopes.value = Event(true)
    }

    fun setReadyUser() {
        _isReadyUser.value = Event(true)
    }

    fun setMoveToNextOrBack(move: Boolean) {
        _isMoveToNextOrBack.value = Event(move)
    }

    fun setKakaoUserInfo(age: Int, email: String, gender: String, nickName: String) {
        val user = SignUpRequest(
            age,
            email,
            requireNotNull(_fcmToken.value),
            gender,
            requireNotNull(_kakaoToken.value),
            nickName
        )
        _havitUser.value = user
        setNickName(nickName)
    }

    fun postSignUp() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(
                    requireNotNull(_havitUser.value?.email),
                    requireNotNull(_havitUser.value?.nickname),
                    requireNotNull(_havitUser.value?.age),
                    requireNotNull(_havitUser.value?.gender),
                    requireNotNull(_fcmToken.value),
                    requireNotNull(_kakaoToken.value)
                )
            }.onSuccess {
                authRepository.saveAccessToken(requireNotNull(it.data.accessToken))
                _accessToken.postValue(it.data.accessToken)
            }.onFailure {
                throw it
            }
        }
    }

    fun getSignIn() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getSignIn(
                    requireNotNull(_fcmToken.value), requireNotNull(_kakaoToken.value)
                )
            }.onSuccess {
                _isAlreadyUser.postValue(Event(it))
                if (it.data.isAlreadyUser == null) authRepository.saveAccessToken(requireNotNull(it.data.accessToken))
            }.onFailure { throw it }

        }
    }

    fun setFcmToken() {
        _fcmToken.value = authRepository.getFcmToken()
    }

    fun setKakaoToken(token: String) {
        _kakaoToken.value = token
        authRepository.saveKakaoToken(token)
    }

    fun getKakaoToken() {
        _kakaoToken.value = authRepository.getKakaoToken()
    }

    fun setAllCheck() {
        isAllCheck.value = !isAllCheck.value!!
        isTosUseCheck.value = !isTosUseCheck.value!!
        isTosInfoCheck.value = !isTosInfoCheck.value!!
        isTosEventCheck.value = !isTosEventCheck.value!!
        isNextClick.value = isAllCheck.value != false
    }

    fun setTosUseCheck() {
        isTosUseCheck.value = !isTosUseCheck.value!!
        isNextClick.value = isTosUseCheck.value != false
    }

    fun setTosInfoCheck() {
        isTosInfoCheck.value = !isTosInfoCheck.value!!
        isNextClick.value = isTosInfoCheck.value != false
    }

    fun setTosEventCheck() {
        isTosEventCheck.value = !isTosEventCheck.value!!
        isNextClick.value = isTosEventCheck.value != false
    }

    fun setNickName(name: String) {
        nickName.value = name
    }

    fun setClearNickName() {
        nickName.value = ""
    }

}