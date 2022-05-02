package org.sopt.havit.domain.repository

import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.domain.entity.Contents

interface SearchRepository {

    suspend fun getSearchContents(keyword: String): Flow<List<Contents>>

    suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<List<Contents>>
}