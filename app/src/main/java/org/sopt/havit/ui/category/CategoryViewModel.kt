package org.sopt.havit.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryModifyRequest
import org.sopt.havit.data.remote.CategoryOrderRequest
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.util.MySharedPreference

class CategoryViewModel : ViewModel() {
    private val _categoryCount = MutableLiveData<Int>()
    val categoryCount: LiveData<Int> = _categoryCount
    private val _categoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val categoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _categoryList

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                        .getAllCategory()
                _categoryList.postValue(response.data)
                _categoryCount.postValue(response.data.size)
            } catch (e: Exception) {
            }
        }
    }

    fun requestCategoryOrder(list : List<Int>){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = CategoryOrderRequest(list)
                RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                    .modifyCategoryOrder(list)
            } catch (e: Exception) { }
        }
    }

    private val asdf=MutableLiveData(false)
    val _asdf :LiveData<Boolean> = asdf

    fun requestCategoryContent(id: Int, imageId: Int, title:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = CategoryModifyRequest(title, imageId)
                val response =RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                    .modifyCategoryContent(id, content)
                //asdf.postValue(true)
                Log.d("testtestest", "$response.success")
            } catch (e: Exception) {
                Log.d("testtestest", "${e.toString()}")
            }
        }
    }

    fun requestCategoryDelete(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                    .deleteCategory(id)
                Log.d("testtestest", "$response.success")
            } catch (e: Exception) {
                Log.d("testtestest", "${e.toString()}")
            }
        }
    }
}