package org.sopt.havit.ui.home

import android.content.Context
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

    // 최근저장 콘텐츠
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

    // 카테고리 전체 데이터
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

    // category 전체 데이터를 6개씩 잘라 List로 묶는 함수
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
            if (count == 0) {
                val firstPage = mutableListOf<CategoryResponse.AllCategoryData>()
                firstPage.clear()
                firstPage.add(firstData)
                val min = if (data.size < 5) (data.size - 1) else 4
                for (i in 0..min) {
                    firstPage.add(data[i])
                }
                list.add(firstPage)
                count += 5
            } else {
                if (data.size - count >= 6) {
                    list.add(data.subList(count, count + 6))
                    count += 6
                } else {
                    list.add(data.subList(count, data.size))
                    break
                }
            }
        }
        return list
    }

    // 추천 콘텐츠
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

    // 유저 데이터
    private val _userData = MutableLiveData<UserResponse.UserData>()
    val userData: LiveData<UserResponse.UserData> = _userData
    fun requestUserDataTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .getUserData()
                _userData.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }

    // 도달률 데이터
    private val _reachRate = MutableLiveData<Int>()
    var reachRate: LiveData<Int> = _reachRate
    fun requestReachRate(rate: Int) {
        _reachRate.postValue(rate)
    }
}