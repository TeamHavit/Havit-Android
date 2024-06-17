package org.sopt.havit.domain.entity

data class CommunityPost(
    val body: String,
    val contentDescription: String,
    val contentTitle: String,
    val contentUrl: String,
    val createdAt: String,
    val id: Int,
    val nickname: String,
    val profileImage: String,
    val thumbnailUrl: String,
    val title: String,
    val isAuthor: Boolean
)
