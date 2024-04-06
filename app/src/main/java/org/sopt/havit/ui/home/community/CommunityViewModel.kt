package org.sopt.havit.ui.home.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {
    // 알림 예정 콘텐츠
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
}