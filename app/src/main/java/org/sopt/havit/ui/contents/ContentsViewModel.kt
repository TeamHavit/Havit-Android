package org.sopt.havit.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.ContentsRepository
import javax.inject.Inject

@HiltViewModel
class ContentsViewModel @Inject constructor(
    private val contentsRepository: ContentsRepository
) : ViewModel() {
    private val _contentsList = MutableLiveData<List<Contents>>()
    val contentsList: LiveData<List<Contents>> = _contentsList

    private val _contentsCount = MutableLiveData(-1)
    val contentsCount: LiveData<Int> = _contentsCount

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    // 로딩 상태를 나타내는 변수
    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState> = _loadState

    private val _requestSeenState = MutableLiveData(NetworkState.LOADING)
    val requestSeenState: LiveData<NetworkState> = _requestSeenState

    private val _requestDeleteState = MutableLiveData<NetworkState>()
    val requestDeleteState: LiveData<NetworkState> = _requestDeleteState

    fun getContentsByCategory(categoryId: Int, option: String, filter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                contentsRepository.getContentsByCategory(
                    categoryId = categoryId,
                    option = option,
                    filter = filter
                )
            }.onSuccess {
                _contentsList.postValue(it)
                _contentsCount.postValue(it.size)

                _loadState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _loadState.postValue(NetworkState.FAIL)
            }
        }
    }

    fun getAllContents(option: String, filter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                contentsRepository.getAllContents(
                    option = option,
                    filter = filter
                )
            }.onSuccess {
                _contentsList.postValue(it)
                _contentsCount.postValue(it.size)

                _loadState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _loadState.postValue(NetworkState.FAIL)
            }
        }
    }

    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                contentsRepository.isSeen(contentsId)
            }.onSuccess {
                _requestSeenState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _requestSeenState.postValue(NetworkState.FAIL)
            }
        }
    }

    // 콘텐츠 삭제를 서버에게 요청하는 코드
    fun deleteContents(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                contentsRepository.deleteContents(contentsId)
            }.onSuccess {
                _requestDeleteState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _requestDeleteState.postValue(NetworkState.FAIL)
            }
        }
    }

    // 콘텐츠 개수 감소시키는 함수
    fun decreaseContentsCount(count: Int) {
        _contentsCount.value = contentsCount.value?.minus(count)
    }

    // 카테고리 이름 설정
    fun setCategoryName(name: String) {
        _categoryName.value = name
    }

    fun updateContentsList(list: List<Contents>) {
        _contentsList.value = list
    }

    fun initRequestState() {
        _requestSeenState.value = NetworkState.LOADING
    }
}
