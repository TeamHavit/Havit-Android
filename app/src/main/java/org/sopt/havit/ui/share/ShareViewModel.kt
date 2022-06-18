package org.sopt.havit.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    val token = authRepository.getAccessToken()

    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()

    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }

    fun isTitleModified() = originTitle.value != currTitle.value
}
