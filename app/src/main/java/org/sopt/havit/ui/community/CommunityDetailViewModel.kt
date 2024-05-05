package org.sopt.havit.ui.community

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject
@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {


}
