package org.sopt.havit.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.sopt.havit.data.mapper.ContentsMapper
import org.sopt.havit.data.source.SearchRemoteDataSource
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.repository.SearchRepository
import org.sopt.havit.util.MySharedPreference
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val dataSource: SearchRemoteDataSource
) :
    SearchRepository {

    private val pref = MySharedPreference.getXAuthToken(context)

    override suspend fun getSearchContents(keyword: String): Flow<List<Contents.Data>> =
        flow {
            dataSource.getSearchContents(keyword).collect { list ->
                emit(list.map { ContentsMapper.mapperToContents(it) })
            }
        }


    override suspend fun getSearchContentsInCategories(
        categoryId: String,
        keyword: String
    ): Flow<List<Contents.Data>> =
        flow {
            dataSource.getSearchContentsInCategories(categoryId, keyword).collect { list ->
                emit(list.map { ContentsMapper.mapperToContents(it) })
            }
        }


}