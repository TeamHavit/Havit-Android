package org.sopt.havit.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.data.remote.RecommendationResponse
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.util.MySharedPreference

class HomeViewModel(context: Context) : ViewModel() {

    private val token = MySharedPreference.getXAuthToken(context)

    private val _contentsList = MutableLiveData<List<ContentsSimpleResponse.ContentsSimpleData>>()
    val contentsList: LiveData<List<ContentsSimpleResponse.ContentsSimpleData>> = _contentsList
    fun requestContentsTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getContentsRecent()
                _contentsList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }

    private val _categoryList = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val categoryList: LiveData<List<CategoryResponse.AllCategoryData>> = _categoryList


    private val _categoryData = MutableLiveData<List<CategoryResponse.AllCategoryData>>()
    val categoryData: LiveData<List<CategoryResponse.AllCategoryData>> = _categoryData
    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getAllCategory()
                _categoryData.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }

    private val _recommendList = MutableLiveData<List<RecommendationResponse.RecommendationData>>()
    val recommendList: LiveData<List<RecommendationResponse.RecommendationData>> = _recommendList
    fun requestRecommendTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getRecommendation()
                _recommendList.postValue(response.data)
            } catch (e: Exception) {

            }
        }
    }

    fun setList(
        data:
        List<CategoryResponse.AllCategoryData>, totalNum: Int
    ): MutableList<List<CategoryResponse.AllCategoryData>> {
        val list = mutableListOf(listOf<CategoryResponse.AllCategoryData>())
        var count = 0
        val firstData = CategoryResponse.AllCategoryData(
            totalNum,
            -1,
            -1,
            "",
            -1,
            "모든 콘텐츠"
        )
        list.clear()
        while (data.size > count) {
            if (data.size - count >= 6) {
                if (count == 0) {
                    val firstPage = mutableListOf<CategoryResponse.AllCategoryData>()
                    firstPage.clear()
                    firstPage.add(firstData)
                    val min = if (data.size < 5) data.size else 4
                    for (i in 0..min) {
                        firstPage.add(data[i])
                    }
                    Log.d("HOMEFRAGMENT_TEMP", "temp : $firstPage")
                    list.add(firstPage)
                    count += 5
                } else {
                    list.add(data.subList(count, count + 6))
                    count += 6
                }
            } else {
                list.add(data.subList(count, data.size))
                break
            }
        }
        return list
    }


    private val _userData = MutableLiveData<UserResponse.UserData>()
    val userData: LiveData<UserResponse.UserData> = _userData
    fun requestUserDataTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getUserData()
                _userData.postValue(response.data)
                Log.d("HOMEVIEWMODEL", "rate: ${_reachRate.value}")
                Log.d("HOMEVIEWMODEL", "userdata: $userData")
            } catch (e: Exception) {
            }
        }
    }

    private val _reachRate = MutableLiveData<Int>()
    var reachRate: LiveData<Int> = _reachRate
    fun requestReachRate(rate: Int) {
        _reachRate.postValue(rate)
    }

    //    dummy data
    private val _popupData = MutableLiveData<String>().apply {
        value = "도달률이 50% 이하로 떨어졌어요!"
    }
    val popupData: LiveData<String> = _popupData
}