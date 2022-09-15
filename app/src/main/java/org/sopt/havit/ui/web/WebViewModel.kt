package org.sopt.havit.ui.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(private val contentsRepository: ContentsRepository) :
    ViewModel() {

    var _isHavit = MutableLiveData<Event<Boolean>>()
    val isHavit: LiveData<Event<Boolean>> = _isHavit

    var contentsUrl = MutableLiveData<String>()

    var isServerNetwork = MutableLiveData<NetworkState>()

    fun init(havit: Boolean) {
        _isHavit.value = Event(havit)
    }

    fun setHavit(contentsId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                contentsRepository.isSeen(contentsId)
            }.onSuccess {
                if (it.success) {
                    isServerNetwork.postValue(NetworkState.SUCCESS)
                    _isHavit.value = Event(!(_isHavit.value!!.peekContent()))
                }
            }.onFailure {
                isServerNetwork.postValue(NetworkState.FAIL)
            }
        }
    }

    fun setUrl(url: String) {
        contentsUrl.value = url
    }
}
