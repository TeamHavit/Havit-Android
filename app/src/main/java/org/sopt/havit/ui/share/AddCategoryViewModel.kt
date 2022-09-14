package org.sopt.havit.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    authRepository: AuthRepository,
) : ViewModel() {

    /** token */
    val token = authRepository.getAccessToken()

    /** Existing Category */
    private val _existingCategoryList = MutableLiveData<List<String>>()
    private val existingCategoryList: LiveData<List<String>> = _existingCategoryList

    private val _enterCategoryNameViewState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val enterCategoryNameViewState: LiveData<NetworkStatus> = _enterCategoryNameViewState

    fun getExistingCategoryList() {
        viewModelScope.launch {
            kotlin.runCatching {
                val response =
                    RetrofitObject.provideHavitApi(token).getAllCategories().data ?: emptyList()
                val tmp: MutableList<String> = mutableListOf()
                for (element in response) tmp.add(element.title)
                _existingCategoryList.value = tmp
            }.onSuccess {
                _enterCategoryNameViewState.value = NetworkStatus.Success()
            }.onFailure {
                _enterCategoryNameViewState.value = NetworkStatus.Error(throwable = it)
            }
        }
    }

    fun isCategoryNameAlreadyExist(currentTitle: String): Boolean {
        val list = existingCategoryList.value?.toList() ?: throw IllegalStateException()
        return list.stream().anyMatch { anotherString: String? ->
            currentTitle.equals(anotherString, ignoreCase = true)
        }
    }

    /** Category Title */
    private val _categoryTitle = MutableLiveData<String>()
    val categoryTitle: LiveData<String> = _categoryTitle

    fun setCategoryTitle(title: String) {
        _categoryTitle.value = title
    }

    private val _selectedIconPosition = MutableLiveData(0)
    val selectedIconPosition: LiveData<Int> = _selectedIconPosition

    fun setSelectedIconPosition(position: Int) {
        _selectedIconPosition.value = position
    }

    private val _addCategoryViewState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val addCategoryViewState: LiveData<NetworkStatus> = _addCategoryViewState

    fun addCategory() {
        viewModelScope.launch {
            kotlin.runCatching {

            }.onSuccess {
                _addCategoryViewState.value = NetworkStatus.Success()
            }.onFailure {
                _addCategoryViewState.value = NetworkStatus.Success()
            }
        }
    }

}