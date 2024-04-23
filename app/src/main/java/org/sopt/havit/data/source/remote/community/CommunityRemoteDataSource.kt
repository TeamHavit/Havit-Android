package org.sopt.havit.data.source.remote.community

import org.sopt.havit.domain.entity.CommunityCategory

interface CommunityRemoteDataSource {
    suspend fun getCommunityCategories(): List<CommunityCategory>
    suspend fun postCommunityReport(id: Int)
}
