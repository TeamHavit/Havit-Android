package org.sopt.havit.data.source.remote.community

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.data.remote.CommunityReportRequest
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.entity.CommunityPostRequest
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

    override suspend fun getCommunityPost(id: Int): CommunityPost {
        return havitApi.getCommunityPostDetail(id).data
            ?: throw NullPointerException("해당하는 게시글이 존재하지 않습니다.")
    }

    override suspend fun writeCommunityPost(communityPostRequest: CommunityPostRequest): BasicResponse {
        return havitApi.writeCommunityPost(communityPostRequest)
    }
}
