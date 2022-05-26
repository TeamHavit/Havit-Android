package org.sopt.havit.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.sopt.havit.data.mapper.ContentsMapper
import org.sopt.havit.data.source.remote.SearchRemoteDataSource
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val dataSource: SearchRemoteDataSource,
    private val contentsMapper: ContentsMapper
) :
    SearchRepository {

    override suspend fun getSearchContents(keyword: String): Flow<List<Contents>> =
        flow {
            kotlin.runCatching {
                dataSource.getSearchContents(keyword)
            }.onSuccess {
                it.collect { emit(it.data.map { contentsMapper.toContents(it) }) }
            }.onFailure {
                throw it
            }
        }


    override suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<List<Contents>> =
        flow {
            dataSource.getSearchContentsInCategories(categoryId, keyword).collect { list ->
                emit(list.data.map { contentsMapper.toContents(it) })
            }
        }


}