package org.sopt.havit.data.repository

import org.sopt.havit.data.source.remote.community.CommunityRemoteDataSource
import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost
import org.sopt.havit.domain.repository.CommunityRepository
import javax.inject.Inject

class CommunityRepositoryImpl @Inject constructor(
    private val communityRemoteDataSource: CommunityRemoteDataSource,
) : CommunityRepository {
    override suspend fun getCommunityCategories(): List<CommunityCategory> {
        return communityRemoteDataSource.getCommunityCategories()
    }

    override suspend fun writeCommunityPost(communityPost: CommunityPost): Result<Boolean> {
        return try {
            val response = communityRemoteDataSource.writeCommunityPost(communityPost)
            when (response.status) {
                201 -> Result.success(true)
                else -> Result.failure(Exception("message: ${response.message} status: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

