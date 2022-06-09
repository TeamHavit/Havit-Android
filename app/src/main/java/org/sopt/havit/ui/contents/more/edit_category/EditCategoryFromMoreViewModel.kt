package org.sopt.havit.ui.contents.more.edit_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryResponse.AllCategoryData
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class EditCategoryFromMoreViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {
    val token = authRepository.getAccessToken()
    var contentsId = MutableLiveData<Int>()
        private set
    var categoryList = MutableLiveData<List<AllCategoryData>>()
        private set
    var originCategoryId = MutableLiveData<List<Int>>()
        private set
    var newCategoryId = MutableLiveData<List<Int>>()
        private set
    var isCategorySelectedAtLeastOne = MutableLiveData<Boolean>()
        private set
    private val _isNetworkCorrespondenceEnd = MutableLiveData<Event<String>>()
    val isNetworkCorrespondenceEnd: MutableLiveData<Event<String>>
        get() = _isNetworkCorrespondenceEnd

    private fun userClicksOnButton(eventString: String) {
        _isNetworkCorrespondenceEnd.value = Event(eventString)
    }

    fun initProperty(contentsMoreData: ContentsMoreData) {
        contentsId.value = contentsMoreData.id
    }

    fun isCategoryModified(): Boolean {
        return originCategoryId == newCategoryId
    }

    fun getCategoryListWithSelectedInfo() {
        viewModelScope.launch {
            // category 선택여부 알려주는 api 붙여야함
            val response = RetrofitObject.provideHavitApi(token).getAllCategory()
            categoryList.postValue(response.data)
        }.run { userClicksOnButton("Get Category") }
    }

    fun patchNewCategoryList() {
        viewModelScope.launch {
            RetrofitObject.provideHavitApi(token).getCategoryNum()
            // patch api 붙여야됨
        }.run { userClicksOnButton("Patch Category") }
    }
}
