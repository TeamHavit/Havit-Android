package org.sopt.havit.ui.share

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.sopt.havit.BuildConfig
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.mapper.CategoryMapper
import org.sopt.havit.data.remote.ContentsSummeryData
import org.sopt.havit.data.remote.CreateContentsRequest
import org.sopt.havit.domain.entity.CategoryWithSelected
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.ui.share.notification.AfterTime
import org.sopt.havit.util.CalenderUtil
import org.sopt.havit.util.HavitAuthUtil
import org.sopt.havit.util.HavitSharedPreference
import java.util.Calendar
import java.util.Date
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val categoryMapper: CategoryMapper,
    private val preference: HavitSharedPreference
) : ViewModel() {
    /** token */
    fun getAccessToken() = authRepository.getAccessToken()

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

    private var _selectedCategoryId = MutableLiveData<List<Int>>()
    private val selectedCategoryId: LiveData<List<Int>> = _selectedCategoryId

    private val _categoryViewState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val categoryViewState: LiveData<NetworkStatus> = _categoryViewState

    fun getCategoryData() {
        viewModelScope.launch {
            kotlin.runCatching {
                _categoryViewState.value = NetworkStatus.Loading()
                RetrofitObject.provideHavitApi(getAccessToken()).getCategoryList().data
            }.onSuccess {
                _categoryList.value = it.toMutableList()
                _categoryNum.value = it.size
                _hasCategory.value = it.isNotEmpty()
                _categoryViewState.value = NetworkStatus.Success()
            }.onFailure {
                _categoryViewState.value = NetworkStatus.Error(it)
            }
        }
    }

    fun clearCategoryViewState() {
        _categoryViewState.value = NetworkStatus.Init()
    }

    fun onCategoryClick(position: Int) {
        toggleItemSelected(position)
        setIsCategorySelectedAtLeastOne()
    }

    private fun toggleItemSelected(position: Int) {
        categoryList.value?.let {
            it[position].isSelected = !it[position].isSelected
        }
    }

    var isCategorySelectedAtLeastOne = MutableLiveData(false)

    private fun setIsCategorySelectedAtLeastOne() {
        _selectedCategoryId.value = categoryList.value?.filter { it.isSelected }?.map {
            categoryMapper.toCategoryId(it)
        }
        isCategorySelectedAtLeastOne.value = _selectedCategoryId.value?.size != 0
    }


    /** url*/
    private var _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    fun setUrl(url: String) {
        _url.value = extractUrl(url)
    }

    private fun extractUrl(content: String?): String {
        val urlPattern = Pattern.compile(
            "(?:^|\\W)((ht|f)tp(s?)://|www\\.)"
                    + "(([\\w\\-]+\\.)+?([\\w\\-.~]+/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )
        val matcher = urlPattern.matcher(content)
        while (matcher.find()) {
            val matchStart = matcher.start(1)
            val matchEnd = matcher.end()
            return content?.substring(matchStart, matchEnd) ?: ""
        }
        if (BuildConfig.IS_DEV) return "https://www.havit.app/"
        throw IllegalStateException()
    }

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

    /** Contents Data */

    private val _ogData = MutableLiveData<ContentsSummeryData>()
    val ogData: LiveData<ContentsSummeryData> = _ogData

    fun setCrawlingContents() {
        viewModelScope.launch {
            getOgData()
            setDefaultIfTitleDataNotExist()
        }
    }

    private fun setDefaultIfTitleDataNotExist() {
        if (ogData.value?.ogTitle.isNullOrBlank())
            _ogData.value?.ogTitle = NO_TITLE_CONTENTS
    }

    private suspend fun getOgData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                Jsoup.connect(url.value).get()
            }.onSuccess {
                val contentsSummeryData = getDataByOgTags(it)
                _ogData.postValue(contentsSummeryData)
            }.onFailure {
                _ogData.postValue(ContentsSummeryData(ogUrl = url.value.toString()))
            }
        }.join()
    }

    private fun getDataByOgTags(it: Document): ContentsSummeryData {
        val doc = it.select("meta[property^=og:]")
        return ContentsSummeryData(ogUrl = url.value.toString()).apply {
            doc.forEachIndexed { index, _ ->
                val tag = doc[index]
                when (doc[index].attr("property")) {
                    "og:image" -> ogImage = tag.attr("content")
                    "og:description" -> ogDescription = tag.attr("content")
                    "og:title" -> ogTitle = tag.attr("content")
                }
            }
            if (this.ogTitle == "") this.ogTitle = it.title()
        }
    }

    private val _saveContentsViewState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val saveContentsViewState: LiveData<NetworkStatus> = _saveContentsViewState

    fun saveContents() {
        viewModelScope.launch {
            kotlin.runCatching {
                val createContentsRequest = getCreateContentsRequest()
                RetrofitObject.provideHavitApi(preference.getXAuthToken())
                    .createContents(createContentsRequest)
            }.onSuccess {
                _saveContentsViewState.value = NetworkStatus.Success()
            }.onFailure {
                _saveContentsViewState.value = NetworkStatus.Error(it)
            }
        }
    }

    private fun getCreateContentsRequest(): CreateContentsRequest {

        val ogData = ogData.value ?: throw IllegalStateException()
        val imageUrl = ogData.ogImage ?: ""
        val reservedNotification = finalNotificationTime.value
        val notification = reservedNotification != null
        val time = reservedNotification?.replace(".", "-")?.substring(0, 16) ?: ""
        val categoryIds = selectedCategoryId.value ?: throw IllegalStateException()

        return CreateContentsRequest(
            title = ogData.ogTitle,
            url = ogData.ogUrl,
            description = ogData.ogDescription,
            imageUrl = imageUrl,
            isNotified = notification,
            notificationTime = time,
            categoryIds = categoryIds
        )
    }

    companion object {
        const val NO_TITLE_CONTENTS = "제목 없는 콘텐츠"
    }
}
