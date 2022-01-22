package org.sopt.havit.ui.category

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.data.remote.CategoryModifyRequest
import org.sopt.havit.data.remote.CategoryOrderRequest
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.util.MySharedPreference

class CategoryViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _categoryCount = MutableLiveData<Int>()
    val categoryCount: LiveData<Int> = _categoryCount
    private val _categoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val categoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _categoryList
    private val _delay = MutableLiveData(false)
    val delay: LiveData<Boolean> = _delay
    private val _addDelay = MutableLiveData(false)
    val addDelay: LiveData<Boolean> = _addDelay
    private val _shareDelay = MutableLiveData(false)
    val shareDelay: LiveData<Boolean> = _shareDelay

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token).getAllCategory()
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
                val response = RetrofitObject.provideHavitApi(token) .modifyCategoryOrder(list)
                _delay.postValue(true)
                Log.d("testtestest", "$response.success")
            } catch (e: Exception) {
                Log.d("testtestest", "${e.toString()}")
            }
        }
    }

    fun requestCategoryContent(id: Int, imageId: Int, title:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = CategoryModifyRequest(title, imageId)
                val response =RetrofitObject.provideHavitApi(token).modifyCategoryContent(id, content)

                Log.d("testtestest", "$response.success")
            } catch (e: Exception) {
                Log.d("testtestest", "${e.toString()}")
            }
        }
    }

    fun requestCategoryDelete(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =RetrofitObject.provideHavitApi(token) .deleteCategory(id)

                Log.d("testtestest", "$response.success")
            } catch (e: Exception) {
                Log.d("testtestest", "${e.toString()}")
            }
        }
    }

    fun setDelay(v: Boolean){
        _delay.value= v
    }

    fun setAddDelay(v: Boolean){
        _addDelay.value= v
    }

    fun requestAddCategory(t: String, i: Int){
        viewModelScope.launch {
            try {
                // 서버 통신
                val response =
                    RetrofitObject.provideHavitApi(token).addCategory(CategoryAddRequest(t, i))
                _addDelay.postValue(true)
                Log.d("CreateCategory", response.success.toString())
            } catch (e: Exception) {
                // 서버 통신 실패 시
                Log.d("CreateCategory", e.toString())
            }
        }
    }

    fun setShareDelay(v: Boolean){
        _shareDelay.value= v
    }
}