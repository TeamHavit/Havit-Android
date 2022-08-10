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
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        const val SPLASH_FROM_SHARE = true
        const val SPLASH_NORMAL_FLOW = false
    }

    var isServerNetwork = MutableLiveData<NetworkState>()

    var loginGuidVisibility = MutableLiveData(false)
        private set

    private var _isAlreadyUser = MutableLiveData<Event<SignInResponse>>()
    var isAlreadyUser: LiveData<Event<SignInResponse>> = _isAlreadyUser

    private var _isNeedScopes = MutableLiveData<Event<Boolean>>()
    var isNeedScopes: LiveData<Event<Boolean>> = _isNeedScopes

    private var _accessToken = MutableLiveData<String>()
    var accessToken: LiveData<String> = _accessToken

    private val _isSuccessKakaoLogin = MutableLiveData<Event<Boolean>>()
    val isSuccessKakaoLogin: LiveData<Event<Boolean>> = _isSuccessKakaoLogin

    private val _fcmToken = MutableLiveData<String>()

    private val _kakaoToken = MutableLiveData<String>()


    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            _isSuccessKakaoLogin.value = Event(false)
        } else if (token != null) {
            Log.d("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            setKakaoToken(token.accessToken)
            _isNeedScopes.value = Event(true)
            _isSuccessKakaoLogin.value = Event(true)
        }
    }


    fun setLoginGuideVisibility(boolean: Boolean) {
        loginGuidVisibility.value = boolean
    }

    fun getSignIn() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.getSignIn(
                    requireNotNull(_fcmToken.value), requireNotNull(authRepository.getKakaoToken())
                )
            }.onSuccess {
                if (it.success) {
                    _isAlreadyUser.postValue(Event(it))
                    if (it.data.isAlreadyUser == null) {
                        authRepository.saveAccessToken(
                            requireNotNull(
                                it.data.accessToken
                            )
                        )
                    }
                }
            }.onFailure { Log.d("error", "${(it as? HttpException)?.message}") }
        }
    }

    fun initFcmToken() {
        authRepository.getFcmToken { token -> _fcmToken.value = token }
    }

    fun setKakaoToken(token: String) {
        _kakaoToken.value = token
        authRepository.saveKakaoToken(token)
    }

}
