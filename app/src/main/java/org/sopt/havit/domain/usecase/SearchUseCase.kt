package org.sopt.havit.domain.usecase

import org.sopt.havit.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {


    suspend fun getSearchContents(keyword: String) = searchRepository.getSearchContents(keyword)


    suspend fun getSearchContentsInCategories(categoryId: String, keyword: String) =
        searchRepository.getSearchContentsInCategories(categoryId, keyword)
}
