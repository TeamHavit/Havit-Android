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

    private val _reachRate = MutableLiveData<Int>()
    var reachRate: LiveData<Int> = _reachRate

    fun requestPopupData(popup: String) {
        _popupData.value = popup
    }

    private val _reachData = MutableLiveData<HomeReachData>().apply {
        value = HomeReachData(123, "최유빈", 125, 12, 64)
        _reachRate.value =
            (value!!.totalSeenContentNumber.toDouble() / value!!.totalContentNumber.toDouble() * 100).toInt()
    }
    val reachData: LiveData<HomeReachData> = _reachData
    fun requestReachData(homeReach: HomeReachData) {
        _reachData.value = homeReach
    }

    private val _categoryData = MutableLiveData<List<HomeCategoryListData>>().apply {
        val list = mutableListOf<CategoryData>()
        for (i in 1..6) {
            list.add(
                CategoryData(
                    3,
                    "슉슉",
                    "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                    0,
                    1
                )
            )
        }
        value = listOf(
            HomeCategoryListData(list),
            HomeCategoryListData(list),
            HomeCategoryListData(list),
            HomeCategoryListData(
                listOf(
                    CategoryData(
                        3,
                        "슉슉",
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        0,
                        1
                    ),
                    CategoryData(
                        3,
                        "슉슉",
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        0,
                        1
                    ),
                    CategoryData(
                        3,
                        "슉슉",
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        0,
                        1
                    )
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