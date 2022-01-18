package org.sopt.havit.data.remote

data class CategoryResponse(
    val data: List<AllCategoryData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class AllCategoryData(
        val contentNumber: Int,
        val id: Int,
        val orderIndex: Int,
        val title: String,
        var url: String
    )
}