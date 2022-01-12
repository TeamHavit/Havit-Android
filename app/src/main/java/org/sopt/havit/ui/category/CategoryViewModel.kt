package org.sopt.havit.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.CategoryData

class CategoryViewModel : ViewModel() {
    private val _categoryCount = MutableLiveData<String>()
    val categoryCount: LiveData<String> = _categoryCount
    private val _categoryList = MutableLiveData<List<CategoryData>>()
    val categoryList: LiveData<List<CategoryData>> = _categoryList

    fun requestCategoryTaken(list: List<CategoryData>, count: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _categoryList.postValue(list)
            _categoryCount.postValue(count)
        }
    }
}