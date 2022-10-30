package org.sopt.havit.data.repository

import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.data.source.local.AuthLocalDataSource
import org.sopt.havit.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val authLocalDataSource: AuthLocalDataSource) :
    MyPageRepository {
    override suspend fun getUserInfo(): UserResponse {
        return RetrofitObject.provideHavitApi(authLocalDataSource.getAccessToken()).getUserData()
    }
}
