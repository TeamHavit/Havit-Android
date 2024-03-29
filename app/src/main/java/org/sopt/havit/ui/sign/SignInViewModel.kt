package org.sopt.havit.ui.sign

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.SignInResponse
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.domain.repository.SystemMaintenanceRepository
import org.sopt.havit.ui.base.BaseViewModel
import org.sopt.havit.util.Event
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    systemMaintenanceRepository: SystemMaintenanceRepository,
) : BaseViewModel(systemMaintenanceRepository) {

    companion object {
        const val SPLASH_FROM_SHARE = true
        const val SPLASH_NORMAL_FLOW = false
    }

    var isServerNetwork = MutableLiveData<NetworkState>()

    var loginGuidVisibility = MutableLiveData(false)
        private set

    private var _isAlreadyUser = MutableLiveData<Event<SignInResponse>>()
    var isAlreadyUser: LiveData<Event<SignInResponse>> = _isAlreadyUser

    private var _accessToken = MutableLiveData<String>()
    var accessToken: LiveData<String> = _accessToken

    private val _isSuccessKakaoLogin = MutableLiveData<Event<Boolean>>()
    val isSuccessKakaoLogin: LiveData<Event<Boolean>> = _isSuccessKakaoLogin

    private val _fcmToken = MutableLiveData<String>()

    private val _kakaoToken = MutableLiveData<String>()

    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            if (error.toString().contains("statusCode=302")) {
                _isSuccessKakaoLogin.value = Event(false)
            }
            Log.d("TAG", "카카오계정으로 로그인 실패 ${error}")
        } else if (token != null) {
            Log.d("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            _isSuccessKakaoLogin.value = Event(true)
            setKakaoToken(token.accessToken)
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
