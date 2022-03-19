package org.sopt.havit.ui.contents

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsResponse
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.util.MySharedPreference

class ContentsViewModel(context: Context) : ViewModel() {
    private val token = MySharedPreference.getXAuthToken(context)
    private val _contentsList = MutableLiveData<List<ContentsResponse.ContentsData>>()
    val contentsList: LiveData<List<ContentsResponse.ContentsData>> = _contentsList

    private val _contentsCount = MutableLiveData<Int>()
    val contentsCount: LiveData<Int> = _contentsCount

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    // 로딩 상태를 나타내는 변수
    private val _loadState = MutableLiveData(true)
    val loadState: LiveData<Boolean> = _loadState

    var contentsMore = MutableLiveData<ContentsSearchResponse.Data>()

    fun requestContentsTaken(categoryId: Int, option: String, filter: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObject.provideHavitApi(token)
                    .getCategoryContents(categoryId, option, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(name)
                _loadState.postValue(false)
            } catch (e: Exception) {
            }
        }
    }

    fun requestContentsAllTaken(option: String, filter: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObject.provideHavitApi(token).getAllContents(option, filter)
                _contentsList.postValue(response.data)
                _contentsCount.postValue(response.data.size)
                _categoryName.postValue(name)
                _loadState.postValue(false)
            } catch (e: Exception) {
            }
        }
    }

    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token).isHavit(ContentsHavitRequest(contentsId))
            } catch (e: Exception) {

            }
        }
    }

    fun setContentsView(data: ContentsSearchResponse.Data) {
        contentsMore.value = data
    }

    // 콘텐츠 삭제를 서버에게 요청하는 코드
    fun requestContentsDelete(contentsId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObject.provideHavitApi(token).deleteContents(contentsId)
            } catch (e: Exception) {
            }
        }
    }

    // 콘텐츠 개수 감소시키는 함수
    fun decreaseContentsCount(count: Int) {
        _contentsCount.value = contentsCount.value?.minus(count)
    }

    // 카테고리 이름 설정
    fun setCategoryName(name: String) {
        _categoryName.value = name
    }
}