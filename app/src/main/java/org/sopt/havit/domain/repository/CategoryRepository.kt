package org.sopt.havit.domain.repository

import org.sopt.havit.domain.entity.Category

interface CategoryRepository {

    suspend fun getAllCategories(): List<Category>

    suspend fun updateCategoryOrder(categoryIndexArray: List<Int>)

    suspend fun updateCategoryInfo(
        id: Int,
        title: String,
        imageId: Int
    )

    suspend fun deleteCategory(id: Int)
}
