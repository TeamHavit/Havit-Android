package org.sopt.havit.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categoryCount = MutableLiveData(-1)
    val categoryCount: LiveData<Int> = _categoryCount

    private val _categoryList = MutableLiveData<ArrayList<Category>>()
    val categoryList: LiveData<ArrayList<Category>> = _categoryList

    private val _shareDelay = MutableLiveData(false)
    val shareDelay: LiveData<Boolean> = _shareDelay

    private val _categoryLoad = MutableLiveData(true)
    val categoryLoad: LiveData<Boolean> = _categoryLoad

    private val _deleteState = MutableLiveData<NetworkState>()
    val deleteState: LiveData<NetworkState> = _deleteState

    private val _orderModifyState = MutableLiveData<NetworkState>()
    val orderModifyState: LiveData<NetworkState> = _orderModifyState

    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState> = _loadState

    private val _modifyState = MutableLiveData<NetworkState>()
    val modifyState: LiveData<NetworkState> = _modifyState

    fun requestCategoryTaken() {
        viewModelScope.launch {
            kotlin.runCatching {
                categoryRepository.getAllCategories()
            }.onSuccess {
                _categoryList.value = it as ArrayList<Category>
                _categoryCount.value = it.size
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
            }
        }
    }

    fun requestCategoryOrder(list: List<Int>) {
        viewModelScope.launch {
            kotlin.runCatching {
                categoryRepository.updateCategoryOrder(categoryIndexArray = list)
            }.onSuccess {
                _orderModifyState.value = NetworkState.SUCCESS
            }.onFailure {
                _orderModifyState.value = NetworkState.FAIL
            }
        }
    }

    fun requestCategoryContent(id: Int, imageId: Int, title: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                categoryRepository.updateCategoryInfo(id = id, title = title, imageId = imageId)
            }.onSuccess {
                _modifyState.value = NetworkState.SUCCESS
            }.onFailure {
                _modifyState.value = NetworkState.FAIL
            }
        }
    }

    fun requestCategoryDelete(id: Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                categoryRepository.deleteCategory(id)
            }.onSuccess {
                _deleteState.value = NetworkState.SUCCESS
            }.onFailure {
                _deleteState.value = NetworkState.FAIL
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
