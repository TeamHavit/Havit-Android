package org.sopt.havit.domain.entity

data class CommunityPost(
    val body: String,
    val communityCategoryIds: List<Int>,
    val contentDescription: String,
    val contentTitle: String,
    val contentUrl: String,
    val thumbnailUrl: String,
    val title: String,
)