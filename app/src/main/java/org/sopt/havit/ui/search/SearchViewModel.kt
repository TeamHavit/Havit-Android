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
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.domain.usecase.SearchUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val contentsRepository: ContentsRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData<List<Contents>>()
    var searchResult: LiveData<List<Contents>> = _searchResult

    var searchResultSize = MutableLiveData(0)

    private var _searchReload = MutableLiveData<Boolean>(false)
    var searchReload: LiveData<Boolean> = _searchReload

    fun setReload() {
        _searchReload.value = !(_searchReload.value)!!
    }

    var searchTv = MutableLiveData(false)
    private var isSeenCheck = MutableLiveData(false)

    private var _isRead = MutableLiveData<Boolean>()
    var isRead: LiveData<Boolean> = _isRead

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
            try {
                val response = contentsRepository.isSeen(contentsId)
                isSeenCheck.postValue(response.success)
            } catch (e: Exception) {
            }
        }
    }

    fun setHavitToast(havit: Boolean) {
        _isRead.value = havit
    }
}
