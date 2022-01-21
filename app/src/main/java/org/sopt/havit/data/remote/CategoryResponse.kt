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
        var imageId: Int,
        @SerializedName("imageUrl")
        var url: String,
        val orderIndex: Int,
        var title: String
    )
}