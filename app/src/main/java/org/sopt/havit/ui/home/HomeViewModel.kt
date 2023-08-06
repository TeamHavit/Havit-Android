package org.sopt.havit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.data.remote.RecommendationResponse
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.notification.NotificationActivity
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val havitApi: HavitApi
) : ViewModel() {

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
                val response = havitApi.getContentsRecent()
                _contentsList.postValue(response.data)
                _contentsLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _contentsLoadState.postValue(NetworkState.FAIL)
            }
        }
    }

    // 카테고리 전체 데이터
    private val _categoryData = MutableLiveData<List<Category>>()
    val categoryData: LiveData<List<Category>> = _categoryData
    fun requestCategoryTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = havitApi.getAllCategories()
                checkLoadState()
                _categoryData.postValue(response.data ?: emptyList())
                _categoryLoadState.postValue(NetworkState.SUCCESS)
                checkLoadState()
            } catch (e: Exception) {
                _categoryLoadState.postValue(NetworkState.FAIL)
            }
        }
    }

    // category 전체 데이터를 6개씩 잘라 List로 묶는 함수
    fun setList(): MutableList<List<Category>> {
        val categoryData = requireNotNull(categoryData.value)
        val totalContentsNum = requireNotNull(userData.value?.totalContentNumber)

        val list = mutableListOf(listOf<Category>())
        var count = 0
        val firstData = Category(totalContentsNum, -1, -1, "", -1, "모든 콘텐츠")
        list.clear()
        while (categoryData.size > count) {
            if (count == 0) {
                val firstPage = mutableListOf<Category>()
                firstPage.clear()
                firstPage.add(firstData)
                val min = if (categoryData.size < 5) (categoryData.size - 1) else 4
                for (i in 0..min) {
                    firstPage.add(categoryData[i])
                }
                list.add(firstPage)
                count += 5
            } else {
                if (categoryData.size - count >= 6) {
                    list.add(categoryData.subList(count, count + 6))
                    count += 6
                } else {
                    list.add(categoryData.subList(count, categoryData.size))
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
                val response = havitApi.getRecommendation()
                checkLoadState()
                _recommendList.postValue(response.data)
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
                val response = havitApi.getNotification(NotificationActivity.before)
                checkLoadState()
                _notificationList.postValue(response.data)
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
                val response = havitApi.getUserData()
                _userData.postValue(response.data)
                _userLoadState.postValue(NetworkState.SUCCESS)
                setReachRate(response.data) // 목표 달성률 계산
                checkLoadState()
            } catch (e: Exception) {
                _userLoadState.postValue(NetworkState.FAIL)
            }
        }
    }

    // 목표 달성률
    private var _reachRate = MutableLiveData<Int>()
    val reachRate: LiveData<Int> = _reachRate

    // 목표 달성률 계산
    fun setReachRate(data: UserResponse.UserData): Int {
        var rate = 0
        // 전체 콘텐츠 수 or 본 콘텐츠 수가 0일 경우 예외처리
        if (data.totalSeenContentNumber != 0 && data.totalContentNumber != 0) { // 콘텐츠 수가 0이 아니라면 rate 계산
            rate =
                (data.totalSeenContentNumber.toDouble() / data.totalContentNumber.toDouble() * 100).toInt()
        } else if (data.totalContentNumber == 0)
            rate = -1
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

    // userdata(totalContents), CategoryData 두개가 모두 로드되어야만 카테고리 뷰 작업 가능
    private val _isReadyToSetCategory = MediatorLiveData<Boolean>()
    val isReadyToSetCategory: LiveData<Boolean> = _isReadyToSetCategory

    fun addSourceOnIsReadyToSetCategory() {
        _isReadyToSetCategory.addSource(userData) {
            setIsReadyToSetCategory(userData, categoryData)
        }
        _isReadyToSetCategory.addSource(categoryData) {
            setIsReadyToSetCategory(userData, categoryData)
        }
    }

    fun removeSourceOnIsReadyToSetCategory() {
        _isReadyToSetCategory.removeSource(userData)
        _isReadyToSetCategory.removeSource(categoryData)
    }

    private fun setIsReadyToSetCategory(
        userData: LiveData<UserResponse.UserData>,
        categoryData: LiveData<List<Category>>
    ) {
        _isReadyToSetCategory.value = userData.value != null && categoryData.value != null
    }

}
