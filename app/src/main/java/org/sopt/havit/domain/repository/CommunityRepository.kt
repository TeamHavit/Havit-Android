package org.sopt.havit.domain.repository

import org.sopt.havit.domain.entity.CommunityCategory

interface CommunityRepository {
    suspend fun getCommunityCategories(): List<CommunityCategory>
}