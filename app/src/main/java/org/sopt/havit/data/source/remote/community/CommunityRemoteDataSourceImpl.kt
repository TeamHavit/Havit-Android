package org.sopt.havit.data.source.remote.community

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPostRequest
import javax.inject.Inject

class CommunityRemoteDataSourceImpl @Inject constructor(
    private val havitApi: HavitApi,
) : CommunityRemoteDataSource {
    override suspend fun getCommunityCategories(): List<CommunityCategory> {
        return havitApi.getCommunityCategoryList().data ?: emptyList()
    }

    override suspend fun writeCommunityPost(communityPostRequest: CommunityPostRequest): BasicResponse {
        return havitApi.writeCommunityPost(communityPostRequest)
    }
}
