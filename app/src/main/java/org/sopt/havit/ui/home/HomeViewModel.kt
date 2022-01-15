package org.sopt.havit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.havit.data.*

class HomeViewModel : ViewModel() {

    private val _popupData = MutableLiveData<String>().apply {
        value = "도달률이 50% 이하로 떨어졌어요!"
    }
    val popupData: LiveData<String> = _popupData

    fun requestPopupData(popup: String) {
        _popupData.value = popup
    }

    private val _reachData = MutableLiveData<HomeReachData>().apply {
        value = HomeReachData(62.toString(), "/ 145", 100.toString())
    }
    val reachData: LiveData<HomeReachData> = _reachData

    fun requestReachData(homeReach: HomeReachData) {
        _reachData.value = homeReach
    }

    private val _userName = MutableLiveData<String>().apply {
        value = "000"
    }
    val userName: LiveData<String> = _userName
    fun requestUserName(userName: String) {
        _userName.value = userName
    }

    private val _categoryData = MutableLiveData<List<HomeCategoryListData>>().apply {
        value = listOf(
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "전체", 19.toString()),
                    HomeCategoryData("", "category1", 20.toString()),
                    HomeCategoryData("", "category2", 21.toString()),
                    HomeCategoryData("", "category3", 22.toString()),
                    HomeCategoryData("", "category4", 22.toString()),
                    HomeCategoryData("", "category5", 22.toString())
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "category6", 19.toString()),
                    HomeCategoryData("", "category7", 20.toString()),
                    HomeCategoryData("", "category8", 21.toString()),
                    HomeCategoryData("", "category9", 22.toString()),
                    HomeCategoryData("", "category10", 22.toString()),
                    HomeCategoryData("", "category11", 22.toString())
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "category12", 19.toString()),
                    HomeCategoryData("", "category13", 20.toString()),
                    HomeCategoryData("", "category14", 21.toString()),
                    HomeCategoryData("", "category15", 22.toString()),
                    HomeCategoryData("", "category16", 22.toString())
                )
            )
        )
    }
    val categoryData: LiveData<List<HomeCategoryListData>> = _categoryData
    fun requestCategoryData(homeCategoryData: List<HomeCategoryListData>) {
        _categoryData.value = homeCategoryData
    }

    private val _contentsList = MutableLiveData<List<HomeContentsData>>()
    val contentsList: LiveData<List<HomeContentsData>> = _contentsList


    fun requestContentsTaken(list: List<HomeContentsData>) {
        _contentsList.value = list
    }

    private val _recommendList = MutableLiveData<List<HomeRecommendData>>()
    val recommendList: LiveData<List<HomeRecommendData>> = _recommendList
    fun requestRecommendTaken(list: List<HomeRecommendData>) {
        _recommendList.value = list
    }

}