package org.sopt.havit.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.ContentsSearchResponse
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(private val api: HavitApi) : SearchRemoteDataSource {
    override suspend fun getSearchContents(keyword: String): Flow<List<ContentsSearchResponse.Data>> {
        return flow{
            emit(api.getSearchContents(keyword).data)
        }
    }

    override suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<List<ContentsSearchResponse.Data>> {
        return flow{
            emit(api.getSearchCategory(categoryId, keyword).data)
        }
    }
}