package org.sopt.havit.ui.contents.more.edit_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.domain.entity.Category
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
    var categoryList = MutableLiveData<List<Category>>()
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

    fun toggleItemSelected(position: Int) {
        categoryList.value?.let {
            it[position].isSelected = !it[position].isSelected
        }
    }

    fun getCategoryListWithSelectedInfo() {
        // TODO 카테고리 목록 가져오는 API임 -> 현재 컨텐츠의 소속 목록을 불러오는 API 배포되면 교체해야함
        viewModelScope.launch {
            val response = RetrofitObject.provideHavitApi(token).getAllCategoryList()
            categoryList.postValue(response.data.toMutableList())
        }
    }

    fun patchNewCategoryList() {
        viewModelScope.launch {
            RetrofitObject.provideHavitApi(token).getCategoryNum()
            // patch api 붙여야됨
        }.run { userClicksOnButton("Patch Category") }
    }
}
