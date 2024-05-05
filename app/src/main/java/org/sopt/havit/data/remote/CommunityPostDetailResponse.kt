package org.sopt.havit.data.remote

import org.sopt.havit.domain.entity.CommunityPost

data class CommunityPostDetailResponse(
    val data: CommunityPost,
    val message: String,
    val status: Int,
    val success: Boolean
)
