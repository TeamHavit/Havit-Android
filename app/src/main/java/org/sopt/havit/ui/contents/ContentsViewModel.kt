package org.sopt.havit.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.ContentsData

class ContentsViewModel : ViewModel() {
    private val _contentsList = MutableLiveData<List<ContentsData>>()
    val contentsList: LiveData<List<ContentsData>> = _contentsList

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    private val _contentsCount = MutableLiveData<String>()
    val contentsCount: LiveData<String> = _contentsCount

    private val _orderState = MutableLiveData<String>()
    val orderState: LiveData<String> = _orderState

    fun requestContentsTaken(list: List<ContentsData>, name: String, count: String, state: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _contentsList.postValue(list)
            _categoryName.postValue(name)
            _contentsCount.postValue(count)
            _orderState.postValue(state)
        }
    }
}