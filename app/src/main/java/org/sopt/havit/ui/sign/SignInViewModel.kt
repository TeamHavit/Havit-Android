package org.sopt.havit.ui.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel(){
    private var _isSaveClick = MutableLiveData<Boolean>()
    var isSaveClick: LiveData<Boolean> = _isSaveClick

    fun setClick(click: Boolean) {
        _isSaveClick.value = click
    }
}