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
    var isTos1Check = MutableLiveData<Boolean>()
    var isTos2Check = MutableLiveData<Boolean>()
    var isTos3Check = MutableLiveData<Boolean>()

    init {
        _isSaveClick.value = true
        isNextClick.value=false
        isAllCheck.value = false
        isTos1Check.value = false
        isTos2Check.value = false
        isTos3Check.value = false

    }

    fun setAllCheck() {
        isAllCheck.value = !isAllCheck.value!!
        isTos1Check.value = !isTos1Check.value!!
        isTos2Check.value = !isTos2Check.value!!
        isTos3Check.value = !isTos3Check.value!!
        isNextClick.value = isAllCheck.value != false
    }

    fun setTos1Check() {
        isTos1Check.value = !isTos1Check.value!!
        isNextClick.value = isTos1Check.value != false
    }

    fun setTos2Check() {
        isTos2Check.value = !isTos2Check.value!!
        isNextClick.value = isTos2Check.value != false
    }

    fun setTos3Check() {
        isTos3Check.value = !isTos3Check.value!!
        isNextClick.value = isTos3Check.value != false
    }

    fun setClick(click: Boolean) {
        _isSaveClick.value = click
    }
}