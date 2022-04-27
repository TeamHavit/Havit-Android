package org.sopt.havit.data.repository

import android.content.Context
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsHavitResponse
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.util.MySharedPreference

class ContentsRepositoryImpl(context: Context) : ContentsRepository {

    private val pref = MySharedPreference.getXAuthToken(context)

    override suspend fun isSeen(contentsId: Int): ContentsHavitResponse {
        return RetrofitObject.provideHavitApi(pref).isHavit(ContentsHavitRequest(contentsId))
    }
}