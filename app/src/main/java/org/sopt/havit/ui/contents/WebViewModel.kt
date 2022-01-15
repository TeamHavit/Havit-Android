package org.sopt.havit.ui.contents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebViewModel:ViewModel() {

    var isHavit = MutableLiveData<Boolean>()

    init{
        isHavit.value=false
    }

    fun setHavit(){
        isHavit.value = isHavit.value==false
    }

    fun unHavit(){
        isHavit.value=false
    }
}