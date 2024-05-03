package org.sopt.havit.ui.community

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.util.havit_edit_text.EditTextData
import org.sopt.havit.util.havit_edit_text.EditTextInfoType
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class CreatePostViewModel @Inject constructor() : ViewModel() {

    val urlEditTextData = MutableLiveData(
        EditTextData(
            text = MutableLiveData(""),
            infoStatus = MutableLiveData(EditTextInfoType.NONE),
            maxLength = 1000,
            hint = "소개하고 싶은 링크를 공유해보세요."
        )
    )

    val titleEditTextData = MutableLiveData(
        EditTextData(
            text = MutableLiveData(""),
            infoStatus = MutableLiveData(EditTextInfoType.NONE),
            maxLength = 45,
            hint = "내용을 입력하세요."
        )
    )

    val url: LiveData<String> = urlEditTextData.value?.text ?: MutableLiveData("")
    val title: LiveData<String> = titleEditTextData.value?.text ?: MutableLiveData("")

    private val _isUrlValid = MutableLiveData(false)
    val isUrlValid: LiveData<Boolean> = _isUrlValid

    private val _isTitleValid = MutableLiveData(false)
    val isTitleValid: LiveData<Boolean> = _isTitleValid


    fun fetchUrlInfoStatus() {
        val url = urlEditTextData.value?.text?.value.toString().trim()
        when {
            url.isBlank() -> setUrlInfoStatus(EditTextInfoType.NONE)
            isMalformedUrl(url) -> setUrlInfoStatus(EditTextInfoType.INVALID_URL)
            else -> checkUrlValidity(url)
        }
    }


    fun fetchTitleInfoStatus() {
        val title = titleEditTextData.value?.text?.value.toString().trim()
        val maxTitleLength = titleEditTextData.value?.maxLength ?: 0
        when {
            title.isBlank() -> setTitleInfoStatus(EditTextInfoType.NONE)
            title.length == maxTitleLength -> setTitleInfoStatus(EditTextInfoType.EXCEED_MAX_LENGTH_45)
            else -> setTitleInfoStatus(EditTextInfoType.NONE)
        }
    }

    private fun setUrlInfoStatus(infoType: EditTextInfoType) {
        urlEditTextData.value?.infoStatus?.postValue(infoType)
    }

    private fun setTitleInfoStatus(infoType: EditTextInfoType) {
        titleEditTextData.value?.infoStatus?.postValue(infoType)
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
            urlEditTextData.value?.infoStatus?.value == EditTextInfoType.NONE
                    && urlEditTextData.value?.text?.value?.isNotBlank() == true

    }

    fun setIsTitleValid() {
        _isTitleValid.value =
            titleEditTextData.value?.infoStatus?.value == EditTextInfoType.NONE
                    && titleEditTextData.value?.text?.value?.isNotBlank() == true
    }

}