package org.sopt.havit.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.data.repository.ContentsRepository
import org.sopt.havit.data.repository.SearchRepository

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val contentsRepository: ContentsRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData<List<ContentsSearchResponse.Data>>()
    var searchResult: LiveData<List<ContentsSearchResponse.Data>> = _searchResult

    var _searchCount = MutableLiveData<Int>()

    var searchImg = MutableLiveData<Boolean>()
    var searchTv = MutableLiveData<Boolean>()
    var searchIng = MutableLiveData<Boolean>()

    var isSearchFirst = MutableLiveData<Boolean>()

    private var _isRead = MutableLiveData<Boolean>()
    var isRead: LiveData<Boolean> = _isRead

    init {
        searchImg.value = false
        searchTv.value = false
        searchIng.value = false
        isSearchFirst.value=false
    }

    fun setSearchNoImage(search: Boolean) {
        searchImg.value = search
    }

    fun setSearchImage(search: Boolean) {
        searchIng.value = search
    }

    fun setSearchNoText(search: Boolean) {
        searchTv.value = search
    }

    fun getSearchContents(keyWord: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = searchRepository.getSearchContents(keyWord)
                Log.d("fff", response.toString())
                _searchResult.postValue(response)
                _searchCount.postValue(response.size)
                Log.d("fffdd", response.size.toString())
            } catch (e: Exception) {
            }

        }
    }

    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = contentsRepository.isSeen(contentsId)
                Log.d("siiiiin", response.success.toString())
                //isRead.postValue(seen)
            } catch (e: Exception) {

            }
        }
    }

    fun setHavitToast(havit: Boolean) {
        _isRead.value = havit
    }


}