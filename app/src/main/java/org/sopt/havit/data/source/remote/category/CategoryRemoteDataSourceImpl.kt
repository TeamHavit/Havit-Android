package org.sopt.havit.data.source.remote.category

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.UpdateCategoriesOrderRequest
import org.sopt.havit.data.remote.UpdateCategoryInfoRequest
import org.sopt.havit.domain.entity.Category
import javax.inject.Inject

class CategoryRemoteDataSourceImpl @Inject constructor(
    private val havitApi: HavitApi
) : CategoryRemoteDataSource {
    override suspend fun getAllCategories(): List<Category> {
        return havitApi.getAllCategories().data ?: emptyList()
    }

    override suspend fun updateCategoryOrder(request: UpdateCategoriesOrderRequest) {
        havitApi.updateCategoryOrder(request)
    }

    override suspend fun updateCategoryInfo(id: Int, request: UpdateCategoryInfoRequest) {
        havitApi.updateCategoryInfo(id, request)
    }

    override suspend fun deleteCategory(id: Int) {
        havitApi.deleteCategory(id)
    }
}
