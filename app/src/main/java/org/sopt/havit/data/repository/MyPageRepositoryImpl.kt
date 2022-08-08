package org.sopt.havit.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.repository.MyPageRepository
import org.sopt.havit.util.MySharedPreference
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    MyPageRepository {
    private val pref = MySharedPreference.getXAuthToken(context)
    override suspend fun getUserInfo(): UserResponse {
        return RetrofitObject.provideHavitApi(pref).getUserData()
    }
}
