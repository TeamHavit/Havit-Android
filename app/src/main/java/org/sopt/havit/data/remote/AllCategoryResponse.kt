package org.sopt.havit.data.remote

import org.sopt.havit.domain.entity.Category

data class AllCategoryResponse(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: List<Category>
)
