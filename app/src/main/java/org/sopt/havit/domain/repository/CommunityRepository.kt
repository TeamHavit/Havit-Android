package org.sopt.havit.domain.repository

import org.sopt.havit.domain.entity.CommunityCategory
import org.sopt.havit.domain.entity.CommunityPost

interface CommunityRepository {
    suspend fun getCommunityCategories(): List<CommunityCategory>

    suspend fun writeCommunityPost(communityPost: CommunityPost): Result<Boolean>
}