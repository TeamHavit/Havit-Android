package org.sopt.havit.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {
    private var _isSaveClick = MutableLiveData<Boolean>()
    var isSaveClick: LiveData<Boolean> = _isSaveClick

    var isNextClick = MutableLiveData<Boolean>()

    var nickName = MutableLiveData<String>()
    var isAllCheck = MutableLiveData<Boolean>()
    var isTosUseCheck = MutableLiveData<Boolean>()
    var isTosInfoCheck = MutableLiveData<Boolean>()
    var isTosEventCheck = MutableLiveData<Boolean>()

    init {
        _isSaveClick.value = true
        isNextClick.value=false
        isAllCheck.value = false
        isTosUseCheck.value = false
        isTosInfoCheck.value = false
        isTosEventCheck.value = false

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

    fun setClick(click: Boolean) {
        _isSaveClick.value = click
    }
}