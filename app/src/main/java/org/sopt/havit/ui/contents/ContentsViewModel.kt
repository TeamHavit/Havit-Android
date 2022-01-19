package org.sopt.havit.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.data.remote.ContentsSearchResponse

class ContentsViewModel : ViewModel() {
    private val _contentsList = MutableLiveData<List<ContentsResponse.ContentsData>>()
    val contentsList: LiveData<List<ContentsResponse.ContentsData>> = _contentsList

    private val _contentsCount = MutableLiveData<Int>()
    val contentsCount: LiveData<Int> = _contentsCount

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    private val _orderState = MutableLiveData<String>()
    val orderState: LiveData<String> = _orderState

    var contentsMore = MutableLiveData< ContentsSearchResponse.Data>()

    fun requestContentsTaken(categoryId: Int, option: String, filter: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk")
                    .getCategoryContents(categoryId, option, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(name)
            } catch (e: Exception) { }
        }
    }

    fun setIsSeen(contentsId:Int){
        viewModelScope.launch {
            try{
                val response = RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk")
                    .isHavit(ContentsHavitRequest(contentsId))
            }catch (e:Exception){

            }
        }
    }

    fun setContentsView(data: ContentsSearchResponse.Data){
        contentsMore.value=data
    }
}