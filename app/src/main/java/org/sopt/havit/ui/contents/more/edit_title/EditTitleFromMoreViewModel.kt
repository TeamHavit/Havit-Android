package org.sopt.havit.ui.contents.more.edit_title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.data.remote.ModifyTitleParams
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class EditTitleFromMoreViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val havitApi: HavitApi
) : ViewModel() {
    val token = authRepository.getAccessToken()
    var contentsId = MutableLiveData<Int>()
        private set
    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()

    fun isTitleModified() = originTitle.value != currTitle.value

    fun initProperty(contentsMoreData: ContentsMoreData) {
        contentsId.value = contentsMoreData.id
        originTitle.value = contentsMoreData.title
        currTitle.value = contentsMoreData.title
    }

    fun patchNewTitle() {
        viewModelScope.launch {
            kotlin.runCatching {
                havitApi.modifyContentTitle(
                    requireNotNull(contentsId.value),
                    ModifyTitleParams(requireNotNull(currTitle.value))
                )
            }.onSuccess {
                userClicksOnButton(SUCCESS)
            }.onFailure {
                userClicksOnButton(FAIL)
            }
        }
    }

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton(string: String) {
        _isNetworkCorrespondenceEnd.value = Event(string)
    }

    companion object {
        const val SUCCESS = "SUCCESS"
        const val FAIL = "FAIL"
    }
}
