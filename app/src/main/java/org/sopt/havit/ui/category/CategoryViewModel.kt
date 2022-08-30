package org.sopt.havit.ui.category

import android.content.Context
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
    val categoryList: LiveData<ArrayList<CategoryResponse.AllCategoryData>>
        get() = _categoryList

    private val _shareDelay = MutableLiveData(false)
    val shareDelay: LiveData<Boolean> = _shareDelay

    private val _deleteState = MutableLiveData<NetworkState>()
    val deleteState: LiveData<NetworkState> = _deleteState

    private val _orderModifyState = MutableLiveData<NetworkState>()
    val orderModifyState: LiveData<NetworkState> = _orderModifyState

    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState>
        get() = _loadState

    private val _modifyState = MutableLiveData<NetworkState>()
    val modifyState: LiveData<NetworkState> = _modifyState

    fun requestCategoryTaken() {
        viewModelScope.launch {
            kotlin.runCatching {
                RetrofitObject.provideHavitApi(token).getAllCategory()
            }.onSuccess {
                _categoryList.value = it.data as ArrayList<CategoryResponse.AllCategoryData>
                _categoryCount.value = it.data.size
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
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
            kotlin.runCatching {
                val content = CategoryModifyRequest(title, imageId)
                RetrofitObject.provideHavitApi(token).modifyCategoryContent(id, content)
            }.onSuccess {
                _modifyState.postValue(NetworkState.SUCCESS)
            }.onFailure {
                _modifyState.postValue(NetworkState.FAIL)
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
