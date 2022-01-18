package org.sopt.havit.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsResponse

class ContentsViewModel : ViewModel() {
    private val _contentsList = MutableLiveData<List<ContentsResponse.ContentsData>>()
    val contentsList: LiveData<List<ContentsResponse.ContentsData>> = _contentsList

    private val _contentsCount = MutableLiveData<Int>()
    val contentsCount: LiveData<Int> = _contentsCount

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    private val _orderState = MutableLiveData<String>()
    val orderState: LiveData<String> = _orderState

    fun requestContentsTaken(categoryId: Int, seen: String, filter: String, categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk")
                    .getCategoryContents(categoryId, seen, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(categoryName)
            } catch (e: Exception) { }
        }
    }
}