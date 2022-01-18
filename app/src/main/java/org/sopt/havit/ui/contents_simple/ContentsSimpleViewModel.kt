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
    fun requestContentsTaken(contentsType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (contentsType == "unseen") {
                    val response =
                        RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
                            .getContentsRecent()    // .getContentsUnseen()
                    _contentsList.postValue(response.data)
                } else {
                    val response =
                        RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MTk5ODM0MCwiZXhwIjoxNjQ0NTkwMzQwLCJpc3MiOiJoYXZpdCJ9.w1hhe2g29wGzF5nokiil8KFf_c3qqPCXdVIU-vZt7Wo")
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

}