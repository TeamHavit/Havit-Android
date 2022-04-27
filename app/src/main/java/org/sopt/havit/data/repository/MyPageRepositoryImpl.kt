package org.sopt.havit.data.repository

import android.content.Context
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.UserResponse
import org.sopt.havit.domain.repository.MyPageRepository
import org.sopt.havit.util.MySharedPreference

class MyPageRepositoryImpl(context: Context): MyPageRepository {
    private val pref = MySharedPreference.getXAuthToken(context)
    override suspend fun getUserInfo(): UserResponse.UserData {
        return RetrofitObject.provideHavitApi(pref).getUserData().data
    }

}