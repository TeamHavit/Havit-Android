package org.sopt.havit.ui.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.domain.repository.ContentsRepository
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(private val contentsRepository: ContentsRepository) :
    ViewModel() {

    var isHavit = MutableLiveData<Boolean>()
    var contentsUrl = MutableLiveData<String>()

    fun init(havit: Boolean) {
        isHavit.value = havit
    }

    fun setHavit(contentsId: Int) {

        isHavit.value = !isHavit.value!!

        viewModelScope.launch(Dispatchers.IO) {
            contentsRepository.isSeen(contentsId)
        }

    }

    fun setUrl(url: String) {
        contentsUrl.value = url
    }

}