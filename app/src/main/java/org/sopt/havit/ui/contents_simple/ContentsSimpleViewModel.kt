package org.sopt.havit.ui.contents_simple

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.ContentsData

class ContentsSimpleViewModel : ViewModel() {

    private val _contentsList = MutableLiveData<List<ContentsData>>()
    val contentsList: LiveData<List<ContentsData>> = _contentsList

    // 상단 바에 들어갈 최근 저장 콘텐츠 / 봐야 하는 콘텐츠 text
    private val _topBarName = MutableLiveData<String>()
    val topBarName: LiveData<String> = _topBarName

    fun requestContentsTaken(list: List<ContentsData>, name: String) {
        _contentsList.value = list
        _topBarName.value = name
        Log.d("contents_simple", "${contentsList.value}")
        viewModelScope.launch(Dispatchers.IO) { // 서버 쓰일 코드
            // _contentsList.postValue(list)
//            _categoryName.postValue(name)
        }
    }

    private val _emptyContents = MutableLiveData<String>().apply {
        value = "최근 저장 콘텐츠가 없습니다.\n새로운 콘텐츠를 저장해 보세요!"
    }
    val emptyContents: LiveData<String> = _emptyContents
    fun requestEmptyContents(emptyContents: String) {
        _emptyContents.value = emptyContents
    }

}