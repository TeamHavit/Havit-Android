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
}