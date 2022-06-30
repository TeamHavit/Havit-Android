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
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.util.MySharedPreference

class CategoryViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    private val _categoryCount = MutableLiveData(-1)
    val categoryCount: LiveData<Int> = _categoryCount

    private val _categoryList = MutableLiveData<ArrayList<CategoryResponse.AllCategoryData>>()
    val categoryList: LiveData<ArrayList<CategoryResponse.AllCategoryData>> = _categoryList

    private val _shareDelay = MutableLiveData(false)
    val shareDelay: LiveData<Boolean> = _shareDelay

    private val _categoryLoad = MutableLiveData(true)
    val categoryLoad: LiveData<Boolean> = _categoryLoad

    private val _deleteState = MutableLiveData<NetworkState>()
    val deleteState: LiveData<NetworkState> = _deleteState

    private val _orderModifyState = MutableLiveData<NetworkState>()
    val orderModifyState: LiveData<NetworkState> = _orderModifyState

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token).getAllCategory()
                _categoryList.postValue(response.data as ArrayList<CategoryResponse.AllCategoryData>)
                _categoryCount.postValue(response.data.size)
                _categoryLoad.postValue(false)
            } catch (e: Exception) {
            }
        }
    }

    fun requestCategoryOrder(list: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                val list = CategoryOrderRequest(list)
                RetrofitObject.provideHavitApi(token).modifyCategoryOrder(list)
            }.onSuccess {
                _orderModifyState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _orderModifyState.postValue(NetworkState.FAIL)
            }
        }
    }

    fun requestCategoryContent(id: Int, imageId: Int, title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = CategoryModifyRequest(title, imageId)
                val response =
                    RetrofitObject.provideHavitApi(token).modifyCategoryContent(id, content)

                Log.d("requestCategoryContent", "$response.success")
            } catch (e: Exception) {
                Log.d("requestCategoryContent", "$e")
            }
        }
    }

    fun requestCategoryDelete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(token).deleteCategory(id)
            }.onSuccess {
                _deleteState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _deleteState.postValue(NetworkState.FAIL)
            }
        }
    }

    fun setShareDelay(v: Boolean) {
        _shareDelay.value = v
    }

    fun setCategoryListItemIconId(position: Int, imageId: Int) {
        _categoryList.value?.get(position)?.imageId = imageId
        _categoryList.value?.get(position)?.url =
            "https://havit-bucket.s3.ap-northeast-2.amazonaws.com/category_image/3d_icon_$imageId.png"
    }

    fun setCategoryListItemName(position: Int, categoryName: String) {
        _categoryList.value?.get(position)?.title = categoryName
    }
}
