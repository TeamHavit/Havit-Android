package org.sopt.havit.ui.share

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.mapper.CategoryMapper
import org.sopt.havit.domain.entity.CategoryWithSelected
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.util.CalenderUtil
import org.sopt.havit.util.Event
import org.sopt.havit.util.HavitAuthUtil
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val categoryMapper: CategoryMapper
) : ViewModel() {
    /** token */
    val token = authRepository.getAccessToken()

    /** auth */
    fun makeSignIn(
        internetError: () -> Unit,
        onUnAuthorized: () -> Unit,
        onAuthorized: () -> Unit
    ) {
        HavitAuthUtil.isLoginNow({ isInternetNotConnected ->
            if (isInternetNotConnected) internetError()
        }) { isLogin ->
            if (!isLogin) onUnAuthorized()
            else onAuthorized()
        }
    }

    /** Category */
    private val _hasCategory = MutableLiveData(true)
    val hasCategory: LiveData<Boolean> = _hasCategory

    private val _categoryNum = MutableLiveData<Int>()
    val categoryNum: LiveData<Int> = _categoryNum

    private val _categoryList = MutableLiveData<MutableList<CategoryWithSelected>>()
    val categoryList: LiveData<MutableList<CategoryWithSelected>> = _categoryList

    private var selectedCategoryId = MutableLiveData<List<Int>>()

    private val _categoryViewState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val categoryViewState: LiveData<NetworkStatus> = _categoryViewState

    fun getCategoryData() {
        viewModelScope.launch {
            kotlin.runCatching {
                val response = RetrofitObject.provideHavitApi(token).getCategoryList().data
                _categoryList.value = response.toMutableList()
                _categoryNum.value = response.size
                _hasCategory.value = response.isNotEmpty()
            }.onSuccess {
                _categoryViewState.value = NetworkStatus.Success()
            }.onFailure {
                _categoryViewState.value = NetworkStatus.Error(it)
            }.run {
                //categoryViewState = NetworkStatus.Init()
            }
        }
    }

    fun onCategoryClick(position: Int) {
        toggleItemSelected(position)
        setIsCategorySelectedAtLeastOne()
    }

    private fun toggleItemSelected(position: Int) {
        Log.d(TAG, "toggleItemSelected: $position")
        categoryList.value?.let {
            it[position].isSelected = !it[position].isSelected
        }
    }

    var isCategorySelectedAtLeastOne = MutableLiveData(true)

    private fun setIsCategorySelectedAtLeastOne() {
        selectedCategoryId.value = categoryList.value?.filter { it.isSelected == true }?.map {
            categoryMapper.toCategoryId(it)
        }
        isCategorySelectedAtLeastOne.value = selectedCategoryId.value?.size != 0
    }


    /** url*/
    private var _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    fun setUrl(url: String) {
        _url.value = extractUrl(url)
    }

    private fun extractUrl(content: String?): String {
        return try {
            val regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"
            val p: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(content)
            if (m.find()) m.group() else ""
        } catch (e: java.lang.Exception) {
            ""
        }
    }

    /** title */
    var originTitle = MutableLiveData<String>()
        private set
    var currTitle = MutableLiveData<String>()
    fun isTitleModified() = originTitle.value != currTitle.value

    /** notification time */
    private var _finalNotificationTime = MutableLiveData<String?>()
    val finalNotificationTime: LiveData<String?>
        get() = _finalNotificationTime

    private var _tempNotificationTime = MutableLiveData<String?>()
    val tempNotificationTime: LiveData<String?>
        get() = _tempNotificationTime

    private var _tempIndex = MutableLiveData<Int?>()
    val tempIndex: LiveData<Int?>
        get() = _tempIndex

    private var _finalIndex = MutableLiveData<Int?>()
    val finalIndex: LiveData<Int?>
        get() = _finalIndex

    fun syncTempDataWithFinalData() {
        _tempNotificationTime.value = finalNotificationTime.value
        _tempIndex.value = finalIndex.value
    }

    fun syncFinalDataWithTempData() {
        _finalNotificationTime.value = tempNotificationTime.value
        _finalIndex.value = tempIndex.value
    }

    fun setNotificationTimeDirectly(time: String?) {
        _tempNotificationTime.value = time
        _tempIndex.value = 4
    }

    fun setNotificationTimeIndirectly(afterTime: AfterTime) {
        val cal = Calendar.getInstance().apply { time = Date() }
        cal.add(requireNotNull(afterTime.type), requireNotNull(afterTime.interval))
        _tempNotificationTime.value = CalenderUtil.dateAndTimeWithDotFormatMD.format(cal.time)
        _tempIndex.value = afterTime.buttonIndex
    }

    fun setSelectedIndex(index: Int? = null, afterTime: AfterTime? = null) {
        _tempIndex.value = index ?: afterTime?.buttonIndex
    }

    fun isNotificationDataChanged(): Boolean {
        return if (tempIndex.value == 4 && finalIndex.value == 4)
            tempNotificationTime.value != finalNotificationTime.value
        else tempIndex.value != finalIndex.value
    }

    fun deleteNotification() {
        _finalIndex.value = null
        _finalNotificationTime.value = null
    }

    /** server event */
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton() {
        _isNetworkCorrespondenceEnd.value = Event("Finish Server")
    }
}
