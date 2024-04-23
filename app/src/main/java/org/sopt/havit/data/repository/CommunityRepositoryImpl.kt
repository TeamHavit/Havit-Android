package org.sopt.havit.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.source.remote.community.CommunityPagingSource
import org.sopt.havit.data.source.remote.community.CommunityRemoteDataSource
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
    private val communityPagingSource: CommunityPagingSource
) : CommunityRepository {
    override suspend fun getCommunityCategories(): List<CommunityCategory> {
        return communityRemoteDataSource.getCommunityCategories()
    }

    override suspend fun getCommunityAllPosts(): Flow<PagingData<CommunityPost>> {
        return Pager(PagingConfig(pageSize = PAGE_SIZE)) { communityPagingSource }.flow
    }

    companion object {
        private const val PAGE_SIZE = 2
    }
}

