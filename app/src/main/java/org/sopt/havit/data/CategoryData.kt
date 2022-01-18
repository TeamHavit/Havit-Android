package org.sopt.havit.data

import com.google.gson.annotations.SerializedName

data class CategoryData(
    val id: Int,
    val title: String,
    val categoryImage: String,
    val orderIndex: Int,
    @SerializedName("content_number")
    val contentNumber: Int
)