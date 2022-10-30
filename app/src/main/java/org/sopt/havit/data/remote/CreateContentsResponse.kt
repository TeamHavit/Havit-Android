package org.sopt.havit.data.remote

data class CreateContentsResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: ContentId
) {
    data class ContentId(
        val contentId: Int
    )
}
