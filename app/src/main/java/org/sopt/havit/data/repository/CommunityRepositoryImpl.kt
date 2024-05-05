package org.sopt.havit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.source.remote.community.CommunityByCommunityPagingSource
import org.sopt.havit.data.source.remote.community.CommunityPagingSource
import org.sopt.havit.data.source.remote.community.CommunityRemoteDataSource
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val havitApi: HavitApi,
    private val communityRemoteDataSource: CommunityRemoteDataSource,
) : CommunityRepository {
    override suspend fun getCommunityCategories(): List<CommunityCategory> {
        return communityRemoteDataSource.getCommunityCategories()
    }

    override suspend fun getCommunityAllPosts(): Flow<PagingData<CommunityPost>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)) { CommunityPagingSource(havitApi) }.flow
    }

    override suspend fun getCommunityPostsByCategory(categoryId: Int): Flow<PagingData<CommunityPost>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            CommunityByCommunityPagingSource(
                havitApi = havitApi,
                categoryId = categoryId
            )
        }.flow
    }

    override suspend fun postCommunityReport(id: Int) {
        communityRemoteDataSource.postCommunityReport(id)
    }

    companion object {
        private const val PAGE_SIZE = 2
    }
}

