package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.UserResponse

interface MyPageRepository {
    suspend fun getUserInfo(): UserResponse.UserData
}