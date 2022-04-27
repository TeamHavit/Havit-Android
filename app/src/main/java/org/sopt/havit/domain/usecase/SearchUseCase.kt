package org.sopt.havit.domain.usecase

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.sopt.havit.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchRepository: SearchRepository) {


    suspend fun getSearchContents(keyword: String) =flow{
        kotlin.runCatching {
            searchRepository.getSearchContents(keyword)
        }.onSuccess { list ->
            list.collect {emit(it)}
        }.onFailure {

        }
    }




    suspend fun getSearchContentsInCategories(categoryId: String, keyword: String) =
        searchRepository.getSearchContentsInCategories(categoryId, keyword)
}