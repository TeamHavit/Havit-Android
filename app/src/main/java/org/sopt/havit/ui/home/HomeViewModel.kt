package org.sopt.havit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.havit.data.HomeCategoryData
import org.sopt.havit.data.HomeCategoryListData
import org.sopt.havit.data.HomeReachData

class HomeViewModel : ViewModel() {

    private val _reachData = MutableLiveData<HomeReachData>().apply {
        value = HomeReachData(62.toString(), 145.toString(), 100.toString())
    }
    val reachData: LiveData<HomeReachData> = _reachData

    private val _userName = MutableLiveData<String>().apply {
        value = "000"
    }
    val userName: LiveData<String> = _userName


    private val _categoryData = MutableLiveData<List<HomeCategoryListData>>().apply {
        value = listOf(
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "전체", 19),
                    HomeCategoryData("", "category1", 20),
                    HomeCategoryData("", "category2", 21),
                    HomeCategoryData("", "category3", 22),
                    HomeCategoryData("", "category4", 22),
                    HomeCategoryData("", "category5", 22)
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "category6", 19),
                    HomeCategoryData("", "category7", 20),
                    HomeCategoryData("", "category8", 21),
                    HomeCategoryData("", "category9", 22),
                    HomeCategoryData("", "category10", 22),
                    HomeCategoryData("", "category11", 22)
                )
            ),
            HomeCategoryListData(
                listOf(
                    HomeCategoryData("", "category12", 19),
                    HomeCategoryData("", "category13", 20),
                    HomeCategoryData("", "category14", 21),
                    HomeCategoryData("", "category15", 22),
                    HomeCategoryData("", "category16", 22)
                )
            )
        )
    }
    val categoryData: LiveData<List<HomeCategoryListData>> = _categoryData
}