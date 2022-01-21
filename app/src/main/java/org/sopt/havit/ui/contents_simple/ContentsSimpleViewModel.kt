package org.sopt.havit.ui.contents_simple

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsSimpleResponse
import org.sopt.havit.util.MySharedPreference

class ContentsSimpleViewModel(context: Context) : ViewModel() {

    private val token = MySharedPreference.getXAuthToken(context)

    private val _contentsList = MutableLiveData<List<ContentsSimpleResponse.ContentsSimpleData>>()
    val contentsList: LiveData<List<ContentsSimpleResponse.ContentsSimpleData>> = _contentsList
    fun requestContentsTaken(contentsType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (contentsType == "unseen") {
                    val response =
                        RetrofitObject.provideHavitApi(token)
                            .getContentsUnseen()
                    _contentsList.postValue(response.data)
                } else {
                    val response =
                        RetrofitObject.provideHavitApi(token)
                            .getContentsRecent()
                    _contentsList.postValue(response.data)
                }
            } catch (e: Exception) {
            }
        }
    }

    // 상단 바에 들어갈 최근 저장 콘텐츠 / 봐야 하는 콘텐츠 text
    private val _topBarName = MutableLiveData<String>()
    val topBarName: LiveData<String> = _topBarName
    fun requestTopBarName(name: String) {
        _topBarName.postValue(name)
    }

    // contentsEmpty일 경우 보일 text
    private val _emptyContents = MutableLiveData<String>()
    val emptyContents: LiveData<String> = _emptyContents
    fun requestEmptyContents(emptyContents: String) {
        _emptyContents.value = emptyContents
    }

    // 해빗하기 버튼
    private val _isHavit = MutableLiveData<Boolean>()
    val isHavit: LiveData<Boolean> = _isHavit
    fun setIsSeen(contentsId: Int) {
        viewModelScope.launch {
            try {
                val response =
                    RetrofitObject.provideHavitApi(token)
                        .isHavit(ContentsHavitRequest(contentsId))
                _isHavit.postValue(response.data.isSeen)
            } catch (e: Exception) {

            }
        }
    }

}