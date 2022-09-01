package org.sopt.havit.data.repository

import org.sopt.havit.data.remote.UpdateCategoriesOrderRequest
import org.sopt.havit.data.remote.UpdateCategoryInfoRequest
import org.sopt.havit.data.source.remote.category.CategoryRemoteDataSource
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {
    override suspend fun getAllCategories(): List<Category> {
        return categoryRemoteDataSource.getAllCategories()
    }

    override suspend fun updateCategoryOrder(categoryIndexArray: List<Int>) {
        categoryRemoteDataSource.updateCategoryOrder(UpdateCategoriesOrderRequest(categoryIndexArray))
    }

    override suspend fun updateCategoryInfo(id: Int, title: String, imageId: Int) {
        categoryRemoteDataSource.updateCategoryInfo(
            id,
            UpdateCategoryInfoRequest(title = title, imageId = imageId)
        )
    }

    override suspend fun deleteCategory(id: Int) {
        categoryRemoteDataSource.deleteCategory(id)
    }
}
