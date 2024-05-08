package org.sopt.havit.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.entity.CommunityPostRequest

interface CommunityRepository {
    suspend fun getCommunityCategories(): List<CommunityCategory>
    suspend fun getCommunityAllPosts(): Flow<PagingData<CommunityPost>>
    suspend fun getCommunityPostsByCategory(categoryId: Int): Flow<PagingData<CommunityPost>>
    suspend fun postCommunityReport(id: Int)
    suspend fun getCommunityPostDetail(id: Int): CommunityPost
    suspend fun writeCommunityPost(communityPostRequest: CommunityPostRequest): Result<Boolean>
}
