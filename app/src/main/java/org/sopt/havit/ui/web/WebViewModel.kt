package org.sopt.havit.ui.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.repository.ContentsRepository

class WebViewModel(private val contentsRepository: ContentsRepository) : ViewModel() {

    var isHavit = MutableLiveData<Boolean>()
    var _isHavit: LiveData<Boolean> = isHavit
    var contentsUrl = MutableLiveData<String>()

    fun init(havit: Boolean) {
        isHavit.value = havit
    }

    fun setHavit(contentsId: Int) {

        isHavit.value = !isHavit.value!!

        viewModelScope.launch {
            contentsRepository.isSeen(contentsId)
        }

    }

    fun setUrl(url: String) {
        contentsUrl.value = url
    }
    
    fun unHavit() {
        isHavit.value = false
    }
}