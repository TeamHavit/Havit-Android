package org.sopt.havit.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.sopt.havit.data.ContentsData
import org.sopt.havit.data.remote.SearchContentsResponse
import org.sopt.havit.data.repository.SearchRepository
import org.sopt.havit.domain.repository.SearchRepositoryImpl

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel(){

    private val _searchResult = MutableLiveData<List<SearchContentsResponse.Contents>>()
    var searchResult :LiveData<List<SearchContentsResponse.Contents>> = _searchResult

    var _searchCount = MutableLiveData<Int>()

    var searchImg = MutableLiveData<Boolean>()
    var searchTv = MutableLiveData<Boolean>()
    var searchIng = MutableLiveData<Boolean>()

    init {
        searchImg.value=false
        searchTv.value=false
        searchIng.value=false
    }

    fun setSearchNoImage(search:Boolean){
        searchImg.value=search
    }

    fun setSearchImage(search:Boolean){
        searchIng.value=search
    }

    fun setSearchNoText(search:Boolean){
        searchTv.value=search
    }

    fun getSearchContents(keyWord:String){
        Log.d("fffs", keyWord.toString())
        viewModelScope.launch {

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


}