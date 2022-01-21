package org.sopt.havit.ui.contents

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.util.MySharedPreference

class ContentsCategoryViewModel (context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)
    private val _contentsCategoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val contentsCategoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _contentsCategoryList

    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token).getAllCategory()
                _contentsCategoryList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }
}