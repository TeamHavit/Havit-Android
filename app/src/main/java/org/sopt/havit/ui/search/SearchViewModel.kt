package org.sopt.havit.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.domain.usecase.SearchUseCase
import org.sopt.havit.util.GoogleAnalyticsUtil
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val contentsRepository: ContentsRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData<List<Contents>>()
    var searchResult: LiveData<List<Contents>> = _searchResult


    private var _searchReload = MutableLiveData(false)
    var searchReload: LiveData<Boolean> = _searchReload


    var searchTv = MutableLiveData(false)
    private var isSeenCheck = MutableLiveData(false)

    private var _isRead = MutableLiveData<Boolean>()
    var isRead: LiveData<Boolean> = _isRead

    var isServerNetwork = MutableLiveData<NetworkState>()

    fun getSearchContents(keyWord: String) {
        viewModelScope.launch {
            searchUseCase.getSearchContents(keyWord).collect {
                _searchResult.postValue(it)
            }
        }
    }

    fun getSearchContentsInCategories(categoryId: String, keyWord: String) {
        viewModelScope.launch {
            try {
                searchUseCase.getSearchContentsInCategories(categoryId, keyWord).collect {
                    _searchResult.postValue(it)
                }
            } catch (e: Exception) {
            }
        }
    }

    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                contentsRepository.isSeen(contentsId)

            }.onSuccess {
                isServerNetwork.postValue(NetworkState.SUCCESS)
                isSeenCheck.postValue(it.success)
                GoogleAnalyticsUtil.logClickEventWithContentCheck(
                    GoogleAnalyticsUtil.CLICK_CONTENT_CHECK,
                    it.data.isSeen
                )

            }.onFailure {
                isServerNetwork.postValue(NetworkState.FAIL)
            }
        }
    }

    fun setHavitToast(havit: Boolean) {
        _isRead.value = havit
    }

    // 콘텐츠 삭제를 서버에게 요청하는 코드
    fun deleteContents(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                contentsRepository.deleteContents(contentsId)
            } catch (e: Exception) {
            }
        }
    }
}
