package org.sopt.havit.data.source

import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.remote.ContentsSearchResponse

interface SearchRemoteDataSource {
    suspend fun getSearchContents(keyword: String): Flow<List<ContentsSearchResponse.Data>>

    suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<List<ContentsSearchResponse.Data>>
}