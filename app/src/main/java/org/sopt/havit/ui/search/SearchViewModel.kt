package org.sopt.havit.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.havit.data.ContentsData
import org.sopt.havit.data.repository.SearchRepository
import org.sopt.havit.domain.repository.SearchRepositoryImpl

class SearchViewModel(private val searchRepositoryImpl: SearchRepositoryImpl) : ViewModel(){

    private val _searchResult = MutableLiveData<List<ContentsData>>()
    var searchResult :LiveData<List<ContentsData>> = _searchResult

    fun getSearchContents(){
        viewModelScope.launch {
            searchRepositoryImpl.getSearchContents()
        }
    }


}