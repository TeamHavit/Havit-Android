package org.sopt.havit.ui.contents

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.util.MySharedPreference

class ContentsViewModel (context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)
    private val _contentsList = MutableLiveData<List<ContentsResponse.ContentsData>>()
    val contentsList: LiveData<List<ContentsResponse.ContentsData>> = _contentsList

    private val _contentsCount = MutableLiveData<Int>()
    val contentsCount: LiveData<Int> = _contentsCount

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    private val _orderState = MutableLiveData<String>()
    val orderState: LiveData<String> = _orderState

    var contentsMore = MutableLiveData< ContentsSearchResponse.Data>()

    fun requestContentsTaken(categoryId: Int, option: String, filter: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = RetrofitObject.provideHavitApi(token).getCategoryContents(categoryId, option, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(name)
            } catch (e: Exception) { }
        }
    }

    fun requestContentsAllTaken(option: String, filter: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                val response = RetrofitObject.provideHavitApi(token).getAllContents(option, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(name)
            } catch (e: Exception) { }
        }
    }

    fun setIsSeen(contentsId:Int){
        viewModelScope.launch {
            try{
                val response = RetrofitObject.provideHavitApi(token).isHavit(ContentsHavitRequest(contentsId))
            }catch (e:Exception){

            }
        }
    }

    fun setContentsView(data: ContentsSearchResponse.Data){
        contentsMore.value=data
    }
}