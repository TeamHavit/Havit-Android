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
                    RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                        .getAllCategory()
                _contentsCategoryList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }
}