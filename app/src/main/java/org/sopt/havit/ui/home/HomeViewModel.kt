package org.sopt.havit.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.*
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.util.MySharedPreference

class HomeViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    // 로딩 상태를 나타내는 변수
    private val _loadState = MutableLiveData(true)
    val loadState: LiveData<Boolean> = _loadState

    private val _contentsLoadState = MutableLiveData(true)
    val contentsLoadState: LiveData<Boolean> = _contentsLoadState
    private val _categoryLoadState = MutableLiveData(true)
    val categoryLoadState: LiveData<Boolean> = _categoryLoadState
    private val _recommendLoadState = MutableLiveData(true)
    val recommendLoadState: LiveData<Boolean> = _recommendLoadState
    private val _notificationLoadState = MutableLiveData(true)
    val notificationLoadState: LiveData<Boolean> = _notificationLoadState
    private val _userLoadState = MutableLiveData(true)
    val userLoadState: LiveData<Boolean> = _userLoadState

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
                _contentsLoadState.postValue(false)
                setLoadState()
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
                setLoadState()
                _categoryData.postValue(response.data)
                _categoryLoadState.postValue(false)
                setLoadState()
            } catch (e: Exception) {
            }
        }
    }

    // category 전체 데이터를 6개씩 잘라 List로 묶는 함수
    fun setList(
        data:
        List<CategoryResponse.AllCategoryData>,
        totalNum: Int
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
                setLoadState()
                _recommendList.postValue(response.data)
                _recommendLoadState.postValue(false)
                setLoadState()
            } catch (e: Exception) {
            }
        }
    }

    // 알림 예정 콘텐츠
    private val _notificationList = MutableLiveData<List<NotificationResponse.NotificationData>>()
    val notificationList: LiveData<List<NotificationResponse.NotificationData>> = _notificationList
    fun requestNotificationTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObject.provideHavitApi(token)
                    .getNotification(NotificationActivity.before)
                setLoadState()
                _notificationList.postValue(response.data)
                _notificationLoadState.postValue(false)
                setLoadState()
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
                _userLoadState.postValue(false)
                setReachRate(response.data) // 도달률 계산
                setLoadState()
            } catch (e: Exception) {
            }
        }
    }

    // 도달률
    private var _reachRate = MutableLiveData<Int>()
    val reachRate: LiveData<Int> = _reachRate

    // 도달률 계산
    fun setReachRate(data: UserResponse.UserData): Int {
        var rate = 0
        // 전체 콘텐츠 수 or 본 콘텐츠 수가 0일 경우 예외처리
        if (data.totalSeenContentNumber != 0 && data.totalContentNumber != 0) { // 콘텐츠 수가 0이 아니라면 rate 계산
            rate =
                (data.totalSeenContentNumber.toDouble() / data.totalContentNumber.toDouble() * 100).toInt()
        }
        _reachRate.postValue(rate)
        return rate
    }

    // skeleton
    fun setLoadState() {
        if (userLoadState.value == false && categoryLoadState.value == false
            && contentsLoadState.value == false && recommendLoadState.value == false
            && notificationLoadState.value == false
        )
            _loadState.postValue(false)
    }
}
