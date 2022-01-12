package org.sopt.havit.domain.repository

import org.sopt.havit.data.ContentsData
import org.sopt.havit.data.repository.SearchRepository

class SearchRepositoryImpl :SearchRepository {
    override suspend fun getSearchContents(): List<ContentsData> {
        TODO("Not yet implemented")
    }
}