package org.sopt.havit.data.source.remote.category

import org.sopt.havit.data.remote.UpdateCategoriesOrderRequest
import org.sopt.havit.data.remote.UpdateCategoryInfoRequest
import org.sopt.havit.domain.entity.Category

interface CategoryRemoteDataSource {
    suspend fun getAllCategories(): List<Category>
    suspend fun updateCategoryOrder(request: UpdateCategoriesOrderRequest)
    suspend fun updateCategoryInfo(id: Int, request: UpdateCategoryInfoRequest)
    suspend fun deleteCategory(id: Int)
}
