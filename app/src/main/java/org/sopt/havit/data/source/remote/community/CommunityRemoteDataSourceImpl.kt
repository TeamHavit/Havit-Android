package org.sopt.havit.data.source.remote.community

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.CommunityReportRequest
import org.sopt.havit.domain.entity.CommunityCategory
import javax.inject.Inject

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val havitApi: HavitApi
) : CommunityRemoteDataSource {
    override suspend fun getCommunityCategories(): List<CommunityCategory> {
        return havitApi.getCommunityCategoryList().data ?: emptyList()
    }

    override suspend fun postCommunityReport(id: Int) {
        havitApi.postCommunityReport(CommunityReportRequest(communityPostId = id))
    }
}
