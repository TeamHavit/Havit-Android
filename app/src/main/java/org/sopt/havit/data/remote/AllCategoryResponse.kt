package org.sopt.havit.data.remote

import org.sopt.havit.domain.entity.CategoryWithSelected

data class AllCategoryResponse(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: List<CategoryWithSelected>
)
