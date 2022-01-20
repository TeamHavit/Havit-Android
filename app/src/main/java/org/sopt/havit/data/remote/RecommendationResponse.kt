package org.sopt.havit.data.remote

data class RecommendationResponse(
    val data: List<RecommendationData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class RecommendationData(
        val id: Int,
        val title: String,
        val url: String,
        val websiteCategory: String,
        val imageUrl: String
    )
}