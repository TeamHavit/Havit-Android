package org.sopt.havit.ui.category

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryModifyRequest
import org.sopt.havit.data.remote.CategoryOrderRequest
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.util.MySharedPreference

class CategoryViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _categoryCount = MutableLiveData(-1)
    val categoryCount: LiveData<Int> = _categoryCount
    private val _categoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val categoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _categoryList
    private val _delay = MutableLiveData(false)
    val delay: LiveData<Boolean> = _delay
    private val _shareDelay = MutableLiveData(false)
    val shareDelay: LiveData<Boolean> = _shareDelay
    private val _categoryLoad = MutableLiveData(true)
    val categoryLoad: LiveData<Boolean> = _categoryLoad

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token).getAllCategory()
                _categoryList.postValue(response.data)
                _categoryCount.postValue(response.data.size)
                _categoryLoad.postValue(false)
            } catch (e: Exception) {
            }
        }
    }

    fun requestCategoryOrder(list: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = CategoryOrderRequest(list)
                val response = RetrofitObject.provideHavitApi(token).modifyCategoryOrder(list)
                _delay.postValue(true)
                Log.d("requestCategoryOrder", "$response.success")
            } catch (e: Exception) {
                Log.d("requestCategoryOrder", "$e")
            }
        }
    }

    fun requestCategoryContent(id: Int, imageId: Int, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = CategoryModifyRequest(title, imageId)
                val response = RetrofitObject.provideHavitApi(token).modifyCategoryContent(id, content)

                Log.d("requestCategoryContent", "$response.success")
            } catch (e: Exception) {
                Log.d("requestCategoryContent", "$e")
            }
        }
    }

    fun requestCategoryDelete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObject.provideHavitApi(token).deleteCategory(id)

                Log.d("requestCategoryDelete", "$response.success")
            } catch (e: Exception) {
                Log.d("requestCategoryDelete", "$e")
            }
        }
    }

    fun setDelay(v: Boolean) {
        _delay.value = v
    }

    fun setShareDelay(v: Boolean) {
        _shareDelay.value = v
    }
}
