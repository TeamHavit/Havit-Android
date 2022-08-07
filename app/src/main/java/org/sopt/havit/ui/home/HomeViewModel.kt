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
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.notification.NotificationActivity
import org.sopt.havit.util.MySharedPreference

class HomeViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)

    // 로딩 상태를 나타내는 변수
    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState> = _loadState

    private val _contentsLoadState = MutableLiveData(NetworkState.LOADING)
    val contentsLoadState: LiveData<NetworkState> = _contentsLoadState
    private val _categoryLoadState = MutableLiveData(NetworkState.LOADING)
    val categoryLoadState: LiveData<NetworkState> = _categoryLoadState
    private val _recommendLoadState = MutableLiveData(NetworkState.LOADING)
    val recommendLoadState: LiveData<NetworkState> = _recommendLoadState
    private val _notificationLoadState = MutableLiveData(NetworkState.LOADING)
    val notificationLoadState: LiveData<NetworkState> = _notificationLoadState
    private val _userLoadState = MutableLiveData(NetworkState.LOADING)
    val userLoadState: LiveData<NetworkState> = _userLoadState

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
                if (contentsLoadState.value != NetworkState.FAIL)
                    _contentsLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _contentsLoadState.postValue(NetworkState.FAIL)
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
                checkLoadState()
                _categoryData.postValue(response.data)
                if (categoryLoadState.value != NetworkState.FAIL)
                    _categoryLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _categoryLoadState.postValue(NetworkState.FAIL)
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
                checkLoadState()
                _recommendList.postValue(response.data)
                if (recommendLoadState.value != NetworkState.FAIL)
                    _recommendLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _recommendLoadState.postValue(NetworkState.FAIL)
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
                checkLoadState()
                _notificationList.postValue(response.data)
                if (notificationLoadState.value != NetworkState.FAIL)
                    _notificationLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _notificationLoadState.postValue(NetworkState.FAIL)
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
                if (userLoadState.value != NetworkState.FAIL)
                    _userLoadState.postValue(NetworkState.SUCCESS)
                setReachRate(response.data) // 도달률 계산
                checkLoadState()
            } catch (e: Exception) {
                _userLoadState.postValue(NetworkState.FAIL)
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
    fun checkLoadState() {
        if (userLoadState.value == NetworkState.SUCCESS && categoryLoadState.value == NetworkState.SUCCESS
            && contentsLoadState.value == NetworkState.SUCCESS && recommendLoadState.value == NetworkState.SUCCESS
            && notificationLoadState.value == NetworkState.SUCCESS
        )
            _loadState.postValue(NetworkState.SUCCESS)
    }

    // 서버 실패
    fun setLoadStateFail() {
        _loadState.postValue(NetworkState.FAIL)
    }
}
