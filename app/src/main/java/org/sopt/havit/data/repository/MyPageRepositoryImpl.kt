package org.sopt.havit.data.repository

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val havitApi: HavitApi
) : MyPageRepository {
    override suspend fun getUserInfo(): UserResponse {
        return havitApi.getUserData()
    }
}
