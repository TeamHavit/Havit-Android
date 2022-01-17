package org.sopt.havit.data.remote

data class ContentsHavitResponse(
    val `data`: Data,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class Data(
        val contentId: Int,
        val isSeen: Boolean
    )
}