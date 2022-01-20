package org.sopt.havit.ui.contents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse

class ContentsCategoryViewModel : ViewModel() {
    private val _contentsCategoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val contentsCategoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _contentsCategoryList

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk")
                        .getAllCategory()
                _contentsCategoryList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }
}