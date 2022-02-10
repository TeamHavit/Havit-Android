package org.sopt.havit.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.data.repository.MyPageRepository

class MyPageViewModel(val myPageRepository: MyPageRepository) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _user = MutableLiveData<UserResponse.UserData>()
    val user : LiveData<UserResponse.UserData> = _user

    fun requestUserInfo(){
        viewModelScope.launch {
            _user.postValue(myPageRepository.getUserInfo())
        }
    }
}