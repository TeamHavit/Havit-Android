package org.sopt.havit.ui.community

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.sopt.havit.data.remote.OgData
import org.sopt.havit.domain.entity.CommunityPostRequest
import org.sopt.havit.domain.model.NetworkStatus
import org.sopt.havit.domain.repository.CommunityRepository
import org.sopt.havit.domain.usecase.UrlUseCase
import org.sopt.havit.ui.model.CommunityCategoryRO
import org.sopt.havit.ui.model.toRO
import org.sopt.havit.util.havit_edit_text.EditTextData
import org.sopt.havit.util.havit_edit_text.EditTextInfoType
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val urlUseCase: UrlUseCase,
) : ViewModel() {

    init {
        getCommunityCategories()
    }

    val urlEditTextData = EditTextData(
        text = MutableLiveData(""),
        infoStatus = MutableLiveData(EditTextInfoType.NONE),
        maxLength = Int.MAX_VALUE,
        hint = "소개하고 싶은 링크를 공유해보세요."
    )

    val titleEditTextData = EditTextData(
        text = MutableLiveData(""),
        infoStatus = MutableLiveData(EditTextInfoType.NONE),
        maxLength = 45 + 1,
        hint = "내용을 입력하세요."
    )

    val descriptionEditTextData = EditTextData(
        text = MutableLiveData(""),
        infoStatus = MutableLiveData(EditTextInfoType.NONE),
        maxLength = 1000 + 1,
        hint = "내용을 입력하세요."
    )

    private val _communityCategoryList = MutableLiveData<List<CommunityCategoryRO>>()
    val communityCategoryList: LiveData<List<CommunityCategoryRO>> = _communityCategoryList

    private fun getCommunityCategories() {
        viewModelScope.launch {
            kotlin.runCatching {
                communityRepository.getCommunityCategories()
            }.onSuccess {
                val list = it.map { communityCategory -> communityCategory.toRO() }
                _communityCategoryList.postValue(list)
            }.onFailure {
                _communityCategoryList.postValue(emptyList())
            }
        }
    }

    lateinit var selectedCategory: LiveData<MutableList<CommunityCategoryRO>>

    fun setCommunityCategoryROList(selectedCategories: LiveData<MutableList<CommunityCategoryRO>>) {
        selectedCategory = selectedCategories
    }

    val url: LiveData<String> = urlEditTextData.text
    val title: LiveData<String> = titleEditTextData.text
    val description: LiveData<String> = descriptionEditTextData.text

    private val _isUrlValid = MutableStateFlow(false)
    val isUrlValid: StateFlow<Boolean> = _isUrlValid
    private val _isTitleValid = MutableStateFlow(false)
    private val _isDescriptionValid = MutableStateFlow(false)
    private val _isCategoryValid = MutableStateFlow(false)

    private fun combineStateFlowsToLiveData(predicate: (Boolean, Boolean, Boolean, Boolean) -> Boolean): LiveData<Boolean> {
        return combine(
            _isUrlValid, _isTitleValid, _isDescriptionValid, _isCategoryValid, transform = predicate
        ).asLiveData()
    }

    val isWriting: LiveData<Boolean> =
        combineStateFlowsToLiveData { urlValid, titleValid, descValid, catValid ->
            urlValid || titleValid || descValid || catValid
        }

    val isPostButtonEnabled: LiveData<Boolean> =
        combineStateFlowsToLiveData { urlValid, titleValid, descValid, catValid ->
            urlValid && titleValid && descValid && catValid
        }

    fun fetchUrlInfoStatus() {
        val url = urlEditTextData.text.value.toString()
        when {
            url.isBlank() -> setUrlInfoStatus(EditTextInfoType.NONE)
            isMalformedUrl(url) -> setUrlInfoStatus(EditTextInfoType.INVALID_URL)
            else -> checkUrlValidity(url)
        }
    }


    fun fetchTitleInfoStatus() {
        val title = titleEditTextData.text.value.toString()
        val maxTitleLength = titleEditTextData.maxLength
        when {
            title.isBlank() -> setTitleInfoStatus(EditTextInfoType.NONE)
            title.length == maxTitleLength -> setTitleInfoStatus(EditTextInfoType.EXCEED_MAX_LENGTH_45)
            else -> setTitleInfoStatus(EditTextInfoType.NONE)
        }
    }

    fun fetchDescriptionInfoStatus() {
        val description = descriptionEditTextData.text.value.toString()
        val maxDescriptionLength = descriptionEditTextData.maxLength
        when {
            description.isBlank() -> setDescriptionInfoStatus(EditTextInfoType.NONE)
            description.length == maxDescriptionLength -> setDescriptionInfoStatus(EditTextInfoType.EXCEED_MAX_LENGTH_1000)
            else -> setDescriptionInfoStatus(EditTextInfoType.NONE)
        }
    }


    val setUrlInfoStatus =
        { infoType: EditTextInfoType -> setInfoStatus(infoType, urlEditTextData) }

    val setTitleInfoStatus =
        { infoType: EditTextInfoType -> setInfoStatus(infoType, titleEditTextData) }

    val setDescriptionInfoStatus =
        { infoType: EditTextInfoType -> setInfoStatus(infoType, descriptionEditTextData) }

    private fun setInfoStatus(infoType: EditTextInfoType, editTextData: EditTextData) {
        editTextData.infoStatus.postValue(infoType)
    }

    private fun isMalformedUrl(url: String) = !Patterns.WEB_URL.matcher(url).matches()

    private fun checkUrlValidity(url: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            when (getResponseCodeFromUrl(url)) {
                in 200 until 400 -> setUrlInfoStatus(EditTextInfoType.NONE)
                else -> setUrlInfoStatus(EditTextInfoType.INVALID_URL)
            }
        }
    }

    // todo : viewmodel에서 걷어내기
    private fun getResponseCodeFromUrl(url: String): Int {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        return connection.responseCode
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is java.net.UnknownHostException -> setUrlInfoStatus(EditTextInfoType.INVALID_URL)
            else -> {
                setUrlInfoStatus(EditTextInfoType.NONE)
                Firebase.crashlytics.log("[CreatePostViewModel - URL validation 도중 에러발생] url: ${url.value}/ throwable: $throwable")
            }
        }
    }


    fun setIsUrlValid() {
        _isUrlValid.value =
            urlEditTextData.infoStatus.value == EditTextInfoType.NONE
                    && urlEditTextData.text.value?.isNotBlank() == true

    }

    fun setIsTitleValid() {
        _isTitleValid.value =
            titleEditTextData.infoStatus.value == EditTextInfoType.NONE
                    && titleEditTextData.text.value?.isNotBlank() == true
    }

    fun setIsDescriptionValid() {
        _isDescriptionValid.value =
            descriptionEditTextData.infoStatus.value == EditTextInfoType.NONE
                    && descriptionEditTextData.text.value?.isNotEmpty() == true
    }

    fun setIsCategoryValid(): MutableList<CommunityCategoryRO>? {
        _isCategoryValid.value = selectedCategory.value?.isNotEmpty() == true
        return selectedCategory.value
    }

    fun getUrlInfoStatus() = urlEditTextData.infoStatus

    private val _ogData = MutableLiveData<OgData>()
    val ogData: LiveData<OgData> = _ogData

    fun loadOgData() {
        viewModelScope.launch {
            kotlin.runCatching {
                urlUseCase.loadOgData(url.value.toString())
            }.onSuccess {
                _ogData.postValue(it)
            }.onFailure {
                _ogData.postValue(OgData(ogUrl = url.value.toString()))
            }
        }
    }

    private val _createPostState = MutableLiveData<NetworkStatus>(NetworkStatus.Init())
    val createPostState: LiveData<NetworkStatus> = _createPostState


    fun writePost() {
        viewModelScope.launch {
            kotlin.runCatching {
                val post = getCommunityPost()
                communityRepository.writeCommunityPost(post)
            }.onSuccess {
                when {
                    it.isSuccess -> _createPostState.postValue(NetworkStatus.Success())
                    it.isFailure -> _createPostState.postValue(NetworkStatus.Error(it.exceptionOrNull()))
                }
            }.onFailure {
                _createPostState.postValue(NetworkStatus.Error(it))
            }
        }
    }

    private fun getCommunityPost() = CommunityPostRequest(
        title = title.value.toString(),
        body = description.value.toString(),
        communityCategoryIds = selectedCategory.value?.map { it.id } ?: emptyList(),
        contentDescription = ogData.value?.ogDescription ?: "",
        contentTitle = ogData.value?.ogTitle ?: "",
        contentUrl = ogData.value?.ogUrl ?: "",
        thumbnailUrl = ogData.value?.ogImage ?: ""
    )


}