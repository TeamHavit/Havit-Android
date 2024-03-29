package org.sopt.havit.ui.sign

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import org.sopt.havit.util.HavitSharedPreference
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preference: HavitSharedPreference
) : ViewModel() {

    var isServerNetwork = MutableLiveData<NetworkState>()

    var nickName = MutableLiveData(authRepository.getUserNickName())

    val nickNameStatus = MutableStateFlow(NickNameTextStatus.EMPTY)

    private var _fcmToken = MutableLiveData<String>()

    private var _accessToken = MutableLiveData<String>()
    var accessToken: LiveData<String> = _accessToken

    private var _isMoveToNextOrBack = MutableLiveData<Event<Boolean>>()
    var isMoveToNextOrBack: LiveData<Event<Boolean>> = _isMoveToNextOrBack

    var isTosUseCheck = MutableLiveData(false)
    var isTosInfoCheck = MutableLiveData(false)
    var isTosEventCheck = MutableLiveData(false)

    var isAllCheck = MediatorLiveData<Boolean>().apply {
        addSource(isTosUseCheck) { value = checkTosAll() }
        addSource(isTosInfoCheck) { value = checkTosAll() }
        addSource(isTosEventCheck) { value = checkTosAll() }
    }

    private fun checkTosAll(): Boolean {
        return isTosUseCheck.value == true && isTosInfoCheck.value == true && isTosEventCheck.value == true
    }

    var isNextClick = MediatorLiveData<Boolean>().apply {
        addSource(isAllCheck) {
            value =
                isAllCheck.value == true || (isTosUseCheck.value == true && isTosInfoCheck.value == true)
        }
    }


    fun postSignUp() {
        viewModelScope.launch {
            kotlin.runCatching {
                authRepository.postSignUp(
                    requireNotNull(authRepository.getUserEmail()),
                    requireNotNull(nickName.value),
                    requireNotNull(authRepository.getUserAge()),
                    requireNotNull(authRepository.getUserGender()),
                    requireNotNull(_fcmToken.value),
                    requireNotNull(authRepository.getKakaoToken()),
                    requireNotNull(isTosEventCheck.value)
                )
            }.onSuccess {
                if (it.success) {
                    authRepository.saveAccessToken(requireNotNull(it.data.accessToken))
                    authRepository.saveRefreshToken(requireNotNull(it.data.refreshToken))
                    _accessToken.postValue(it.data.accessToken)
                    isServerNetwork.postValue(NetworkState.SUCCESS)
                }
            }.onFailure {
                isServerNetwork.postValue(NetworkState.FAIL)
                Log.d("error", "${(it as? HttpException)?.message}")
            }
        }
    }

    fun initFcmToken() {
        authRepository.getFcmToken { token -> _fcmToken.value = token }
    }

    fun setAuthToken(token: String) {
        preference.setXAuthToken(token)
    }


    fun clickTosListener(view: View) {
        when (view.id) {
            R.id.iv_tos_info -> isTosInfoCheck.value = !isTosInfoCheck.value!!
            R.id.iv_tos_use -> isTosUseCheck.value = !isTosUseCheck.value!!
            R.id.iv_tos_event -> isTosEventCheck.value = !isTosEventCheck.value!!
            R.id.iv_tos_all -> {
                setAllTosClick()
            }
            R.id.tv_tos_all -> {
                setAllTosClick()
            }
        }

    }

    private fun setAllTosClick() {
        if (isAllCheck.value == false) {
            isAllCheck.value = true
            isTosUseCheck.value = true
            isTosInfoCheck.value = true
            isTosEventCheck.value = true
        } else {
            isAllCheck.value = false
            isTosUseCheck.value = false
            isTosInfoCheck.value = false
            isTosEventCheck.value = false
        }
    }

    fun setNickName(name: String) {
        nickName.value = name
    }

    fun setClearNickName() {
        nickName.value = ""
        nickNameStatus.value = NickNameTextStatus.EMPTY
    }

    fun setMoveToNextOrBack(move: Boolean) {
        _isMoveToNextOrBack.value = Event(move)
    }
}
