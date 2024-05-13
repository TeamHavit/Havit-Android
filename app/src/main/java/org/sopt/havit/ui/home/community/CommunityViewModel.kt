package org.sopt.havit.ui.home.community

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    private val _communityCategoryList = MutableLiveData<List<CommunityCategory>>()
    val communityCategoryList: LiveData<List<CommunityCategory>> = _communityCategoryList

    private val _loadState = MutableLiveData(NetworkState.LOADING)
    val loadState: LiveData<NetworkState>
        get() = _loadState

    init {
        getCommunityCategories()
    }

    private fun getCommunityCategories() {
        viewModelScope.launch {
            _loadState.value = NetworkState.LOADING
            kotlin.runCatching {
                communityRepository.getCommunityCategories()
            }.onSuccess { data ->
                _communityCategoryList.value = (data)
                _loadState.value = NetworkState.SUCCESS
            }.onFailure {
                _loadState.value = NetworkState.FAIL
            }
        }
    }

    suspend fun getCommunityAllPosts(): Flow<PagingData<CommunityPost>> {
        return communityRepository.getCommunityAllPosts().cachedIn(viewModelScope)
    }

    suspend fun getCommunityPostsByCategory(categoryId: Int): Flow<PagingData<CommunityPost>> {
        return communityRepository.getCommunityPostsByCategory(categoryId).cachedIn(viewModelScope)
    }

    fun postCommunityReport(id: Int) {
        viewModelScope.launch {
            _loadState.value = NetworkState.LOADING
            kotlin.runCatching {
                communityRepository.postCommunityReport(id)
            }.onFailure {
                Log.e("CommunityViewModel", "Community Post Id $id 의 삭제 에러")
            }
        }
    }
}