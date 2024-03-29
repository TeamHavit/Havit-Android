package org.sopt.havit.data.remote

data class ContentsSimpleResponse(
    val data: List<ContentsSimpleData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class ContentsSimpleData(
        val createdAt: String,
        var description: String?,
        val id: Int,
        val image: String,
        val isNotified: Boolean,
        var isSeen: Boolean,
        val notificationTime: String,
        val title: String,
        val url: String,
        val firstCategory: String,
        val extraCategoryCount: Int,
    )
}