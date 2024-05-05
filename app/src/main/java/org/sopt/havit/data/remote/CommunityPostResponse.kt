package org.sopt.havit.data.remote

import org.sopt.havit.domain.entity.CommunityPost

data class CommunityPostResponse(
    val posts: List<CommunityPost>,
    val currentPage: Int,
    val totalPageCount: Int,
    val totalItemCount: Int,
    val isLastPage: Boolean,
)