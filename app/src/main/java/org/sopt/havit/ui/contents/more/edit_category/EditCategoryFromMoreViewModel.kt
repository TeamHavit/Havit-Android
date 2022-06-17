package org.sopt.havit.ui.contents.more.edit_category

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.mapper.CategoryMapper
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.data.remote.ModifyContentCategoryParams
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.repository.AuthRepository
import org.sopt.havit.util.Event
import javax.inject.Inject

@HiltViewModel
class EditCategoryFromMoreViewModel @Inject constructor(
    authRepository: AuthRepository,
    private val categoryMapper: CategoryMapper
) : ViewModel() {
    private val token = authRepository.getAccessToken()
    private val contentsId = MutableLiveData<Int>()
    var categoryList = MutableLiveData<List<Category>>()
        private set
    private var originCategoryId = MutableLiveData<List<Int>>()
    private var newCategoryId = MutableLiveData<List<Int>>()
    var isCategorySelectedAtLeastOne = MutableLiveData(true)

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
        return originCategoryId.value != newCategoryId.value
    }

    fun onCategoryClick(position: Int) {
        toggleItemSelected(position)
        setNewCategoryId()
        setIsCategorySelectedAtLeastOne()
    }

    private fun setNewCategoryId() {
        newCategoryId.value = categoryList.value?.filter { it.isSelected }?.map {
            categoryMapper.toCategoryId(it)
        }
    }

    private fun setIsCategorySelectedAtLeastOne() {
        isCategorySelectedAtLeastOne.value = newCategoryId.value?.size != 0
    }

    private fun toggleItemSelected(position: Int) {
        categoryList.value?.let {
            it[position].isSelected = !(it[position].isSelected)
        }
    }

    fun getCategoryListWithSelectedInfo() {
        // TODO 카테고리 목록 가져오는 API임
        //  -> 현재 컨텐츠의 소속 목록을 불러오는 API 배포되면 교체해야함
        viewModelScope.launch {
            kotlin.runCatching {
                val response = RetrofitObject.provideHavitApi(token).getAllCategoryList()
                categoryList.value = response.data
                originCategoryId.value =
                    response.data.filter { !it.isSelected }.map { categoryMapper.toCategoryId(it) }
                // todo : 위에 it.selected `!` 바꿔야됨
                Log.d(TAG, "getCategoryList: ${categoryList.value}")
                Log.d(TAG, "getCategoryList: ${originCategoryId.value}")
            }
        }
    }

    fun patchNewCategoryList() {
        viewModelScope.launch {
            kotlin.runCatching {
                val body = ModifyContentCategoryParams(
                    contentId = requireNotNull(contentsId.value),
                    newCategoryIds = requireNotNull(newCategoryId.value)
                )
                RetrofitObject.provideHavitApi(token).modifyContentCategory(body)
            }.onSuccess {
                Log.d(TAG, "patchNewCategoryList: success")
            }.onFailure {
                Log.d(TAG, "patchNewCategoryList: failure ${it.message}")
            }.run { userClicksOnButton(PATCH_CATEGORY) }
        }
    }

    companion object {
        const val PATCH_CATEGORY = "Patch Category"
    }
}
