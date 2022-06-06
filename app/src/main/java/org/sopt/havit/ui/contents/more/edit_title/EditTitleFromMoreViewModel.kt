package org.sopt.havit.ui.contents.more.edit_title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.data.remote.ModifyTitleParams
import org.sopt.havit.util.Event

class EditTitleFromMoreViewModel : ViewModel() {
    // TODO sharedActivity 에도 ViewModel 만들기
    //  & token 거지같은 로직 바꾸기
    val token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsImlkRmlyZWJhc2UiOiJrYWthbzp0ZW1wIiwiaWF0IjoxNjUxNzcwNTIzLCJleHAiOjE2NjA0MTA1MjMsImlzcyI6Imhhdml0In0.GyGJ_OTJc1cbvXS12VsTSn_hqFLPr_3gNOz3YufMI_A"
    var contentsId = MutableLiveData<Int>()
        private set
    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()

    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    // Trigger the event by setting a new Event as a new value
    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }

    fun isTitleModified() = originTitle.value != currTitle.value

    fun initProperty(contentsMoreData: ContentsMoreData) {
        contentsId.value = contentsMoreData.id
        originTitle.value = contentsMoreData.title
        currTitle.value = contentsMoreData.title
    }

    fun patchNewTitle() {
        viewModelScope.launch {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(token).modifyContentTitle(
                    requireNotNull(contentsId.value),
                    ModifyTitleParams(requireNotNull(currTitle.value))
                )
            }.run { userClicksOnButton() }
        }
    }
}
