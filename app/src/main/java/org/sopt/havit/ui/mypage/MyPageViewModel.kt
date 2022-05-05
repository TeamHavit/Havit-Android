package org.sopt.havit.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.repository.MyPageRepository
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myPageRepository: MyPageRepository) :
    ViewModel() {

    private val _user = MutableLiveData<UserResponse.UserData>()
    val user: LiveData<UserResponse.UserData> = _user

    private val _rate = MutableLiveData<Int>()
    val rate: LiveData<Int> = _rate

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    fun requestUserInfo() {
        viewModelScope.launch {
            try {
                val response = myPageRepository.getUserInfo()
                _user.postValue(response)
                _rate.postValue((response.totalSeenContentNumber.toDouble() / response.totalContentNumber.toDouble() * 100).toInt())
            } catch (e: Exception) {
                _rate.postValue(0)
            }
        }
    }
}