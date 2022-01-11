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

    fun requestContentsTaken(list: List<ContentsData>) {
        viewModelScope.launch(Dispatchers.IO) {
            _contentsList.postValue(list)
        }
    }
}