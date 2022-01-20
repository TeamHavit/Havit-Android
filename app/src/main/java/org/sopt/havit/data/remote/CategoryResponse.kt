package org.sopt.havit.data.remote

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    val data: List<AllCategoryData>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class AllCategoryData(
        val contentNumber: Int,
        val id: Int,
        val imageId: Int,
        @SerializedName("imageUrl")
        val url: String,
        val orderIndex: Int,
        val title: String
    )
}