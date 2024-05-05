package org.sopt.havit.data.source.remote.community

import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost

interface CommunityRemoteDataSource {
    suspend fun getCommunityCategories(): List<CommunityCategory>
    suspend fun writeCommunityPost(communityPost: CommunityPost): BasicResponse
    suspend fun postCommunityReport(id: Int)
}
