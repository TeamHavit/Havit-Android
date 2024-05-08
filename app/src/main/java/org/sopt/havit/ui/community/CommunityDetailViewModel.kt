package org.sopt.havit.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {


    private val _communityPost = MutableLiveData<CommunityPost>()
    val communityPost: LiveData<CommunityPost> = _communityPost

    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState>
        get() = _loadState

    val reportIds: LiveData<MutableList<Int>> = MutableLiveData(mutableListOf())

    fun getCommunityPostDetail(id: Int) {
        viewModelScope.launch {
            _loadState.value = NetworkState.LOADING
            kotlin.runCatching {
                communityRepository.getCommunityPostDetail(id)
            }.onSuccess { data ->
                _communityPost.value = (data)
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
            }
        }
    }

    fun postCommunityReport(id: Int) {
        viewModelScope.launch {
            _loadState.value = NetworkState.LOADING
            kotlin.runCatching {
                communityRepository.postCommunityReport(id)
            }.onSuccess {
                reportIds.value?.add(id)
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
            }
        }
    }

}
