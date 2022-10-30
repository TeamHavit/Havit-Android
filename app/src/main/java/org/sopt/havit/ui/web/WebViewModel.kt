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

    private var _isHavit = MutableLiveData<Event<Boolean>>()
    val isHavit: LiveData<Event<Boolean>> = _isHavit

    private var _isClick = MutableLiveData<Event<Boolean>>()
    val isClick: LiveData<Event<Boolean>> = _isClick

    var contentsUrl = MutableLiveData<String>()

    var isServerNetwork = MutableLiveData<NetworkState>()

    fun init(havit: Boolean) {
        _isHavit.value = Event(havit)
        _isClick.value = Event(!havit)
    }

    fun setHavit(contentsId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                contentsRepository.isSeen(contentsId)
            }.onSuccess {
                if (it.success) {
                    isServerNetwork.postValue(NetworkState.SUCCESS)
                    _isHavit.value = Event(!(_isHavit.value!!.peekContent()))
                    _isClick.value = Event(!(_isClick.value!!.peekContent()))
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
