package org.sopt.havit.ui.contents_simple

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.domain.entity.NetworkState
import javax.inject.Inject

@HiltViewModel
class ContentsSimpleViewModel @Inject constructor(
    private val havitApi: HavitApi
) : ViewModel() {

    private val _contentsList = MutableLiveData<List<ContentsSimpleResponse.ContentsSimpleData>>()
    val contentsList: LiveData<List<ContentsSimpleResponse.ContentsSimpleData>> = _contentsList

    private val _contentsCount = MutableLiveData(-1)
    private val contentsCount: LiveData<Int> = _contentsCount

    // 상단 바에 들어갈 최근 저장 콘텐츠 / 봐야 하는 콘텐츠 text
    private val _topBarName = MutableLiveData<String>()
    val topBarName: LiveData<String> = _topBarName

    // loading 진행 여부 변수.
    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState> = _loadState

    fun requestContentsTaken(contentsType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (contentsType == "unseen") {
                    val response =
                        havitApi.getContentsUnseen()
                    _contentsList.postValue(response.data)
                } else {
                    val response = havitApi.getContentsRecent()
                    _contentsList.postValue(response.data)
                }
                _loadState.postValue(NetworkState.SUCCESS)
            } catch (e: Exception) {
                _loadState.postValue(NetworkState.FAIL)
            }
        }
    }

    fun requestTopBarName(name: String) {
        _topBarName.postValue(name)
    }

    // contentsEmpty일 경우 보일 text
    private val _emptyContents = MutableLiveData<String>()
    val emptyContents: LiveData<String> = _emptyContents
    fun requestEmptyContents(emptyContents: String) {
        _emptyContents.value = emptyContents
    }

    // 해빗하기 버튼
    private val _isHavit = MutableLiveData<Boolean>()
    val isHavit: LiveData<Boolean> = _isHavit
    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch {
            try {
                val response = havitApi.isHavit(ContentsHavitRequest(contentsId))
                _isHavit.postValue(response.data.isSeen)
                _loadState.postValue(NetworkState.SUCCESS)
            } catch (e: Exception) {
                _loadState.postValue(NetworkState.FAIL)
            }
        }
    }

    // 콘텐츠 개수 감소 함수
    fun decreaseContentsCount(count: Int) {
        _contentsCount.value = contentsCount.value?.minus(count)
    }

    fun updateContentsList(list: List<ContentsSimpleResponse.ContentsSimpleData>) {
        _contentsList.value = list
    }

    // 콘텐츠 삭제를 서버에게 요청하는 코드
    fun deleteContents(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                havitApi.deleteContents(contentsId)
                _loadState.postValue(NetworkState.SUCCESS)
            } catch (e: Exception) {
                _loadState.postValue(NetworkState.FAIL)
            }
        }
    }
}
