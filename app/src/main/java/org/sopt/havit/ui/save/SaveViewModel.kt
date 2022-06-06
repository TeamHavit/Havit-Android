package org.sopt.havit.ui.save

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SaveViewModel : ViewModel() {


    var clipDataUrl = MutableLiveData("")
    var saveDataUrl = MutableLiveData("")

    fun setUrlData(url: String) {
        clipDataUrl.value = url
        saveDataUrl.value = url
    }

    fun setClipDataClear() {
        clipDataUrl.value = ""
    }

    fun setSaveDataClear() {
        saveDataUrl.value = ""
    }


}