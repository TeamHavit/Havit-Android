package org.sopt.havit.data.source.remote

import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.remote.ContentsSearchResponse

interface SearchRemoteDataSource {
    suspend fun getSearchContents(keyword: String): Flow<ContentsSearchResponse>

    suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<ContentsSearchResponse>
}