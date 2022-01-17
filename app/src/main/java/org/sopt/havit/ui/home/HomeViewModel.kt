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

     val _reachRate = MutableLiveData<Int>()
    //val reachRate: LiveData<String> = _reachRate

    fun requestPopupData(popup: String) {
        _popupData.value = popup
    }

    private val _reachData = MutableLiveData<HomeReachData>().apply {
        value = HomeReachData(123, "최유빈", 125, 12, 64)
        _reachRate.value = (value!!.totalSeenContentNumber.toDouble() / value!!.totalContentNumber.toDouble() * 100).toInt()
    }
    val reachData: LiveData<HomeReachData> = _reachData
    fun requestReachData(homeReach: HomeReachData) {
        _reachData.value = homeReach
    }

    private val _categoryData = MutableLiveData<List<HomeCategoryListData>>().apply {
        value = listOf(
            HomeCategoryListData(
                listOf(
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "전체",
                        19.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category1",
                        20.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category2",
                        21.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category3",
                        22.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category4",
                        22.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category5",
                        22.toString()
                    )
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category6",
                        19.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category7",
                        20.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category8",
                        21.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category9",
                        22.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category10",
                        22.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category11",
                        22.toString()
                    )
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category12",
                        19.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category13",
                        20.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category14",
                        21.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category15",
                        22.toString()
                    ),
                    HomeCategoryData(
                        "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                        "category16",
                        22.toString()
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