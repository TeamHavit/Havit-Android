package org.sopt.havit.ui.category

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.util.MySharedPreference

class CategoryContentModifyViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)
    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    fun requestCategoryTaken(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token) .deleteCategory(id)
            } catch (e: Exception) {
            }
        }
    }
}