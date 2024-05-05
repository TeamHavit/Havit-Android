package org.sopt.havit.data.source.remote.community

import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost

interface CommunityRemoteDataSource {
    suspend fun getCommunityCategories(): List<CommunityCategory>
    suspend fun postCommunityReport(id: Int)
    suspend fun getCommunityPost(id: Int): CommunityPost
}
