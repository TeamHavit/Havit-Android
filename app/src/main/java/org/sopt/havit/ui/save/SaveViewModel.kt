package org.sopt.havit.ui.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SaveViewModel : ViewModel() {

    var _isSaveClick = MutableLiveData<Boolean>()
    var isSaveClick: LiveData<Boolean> = _isSaveClick

    fun setClick(click: Boolean) {
        _isSaveClick.value = click
    }
}