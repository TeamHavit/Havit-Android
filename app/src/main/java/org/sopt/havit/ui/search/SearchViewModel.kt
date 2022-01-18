package org.sopt.havit.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.SearchContentsResponse
import org.sopt.havit.data.repository.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel(){

    private val _searchResult = MutableLiveData<List<SearchContentsResponse.Contents>>()
    var searchResult :LiveData<List<SearchContentsResponse.Contents>> = _searchResult

     var _searchCount = MutableLiveData<Int>()

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