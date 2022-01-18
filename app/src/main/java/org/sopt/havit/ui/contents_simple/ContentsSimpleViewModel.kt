package org.sopt.havit.ui.contents_simple

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.data.ContentsData
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsSimpleResponse

class ContentsSimpleViewModel : ViewModel() {

    private val _contentsList = MutableLiveData<List<ContentsSimpleResponse.ContentsSimpleData>>()
    val contentsList: LiveData<List<ContentsSimpleResponse.ContentsSimpleData>> = _contentsList

    fun requestContentsTaken() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                        .getContentsRecent()
                _contentsList.postValue(response.data)
            } catch (e: Exception) {
            }
        }
    }


    //    dummy data
    // 상단 바에 들어갈 최근 저장 콘텐츠 / 봐야 하는 콘텐츠 text
    private val _topBarName = MutableLiveData<String>()
    val topBarName: LiveData<String> = _topBarName


    private val _contentsListDummy = MutableLiveData<List<ContentsData>>()
    val contentsListDummy: LiveData<List<ContentsData>> = _contentsListDummy
    fun requestContentsTaken(list: List<ContentsData>, name: String) {
        _contentsListDummy.value = list
        _topBarName.value = name
        Log.d("contents_simple", "${contentsListDummy.value}")
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