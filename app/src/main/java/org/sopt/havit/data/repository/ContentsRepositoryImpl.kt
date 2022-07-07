package org.sopt.havit.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsHavitResponse
import org.sopt.havit.data.source.remote.contents.ContentsRemoteDataSource
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.repository.ContentsRepository
import org.sopt.havit.util.MySharedPreference
import javax.inject.Inject

class ContentsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentsRemoteDataSource: ContentsRemoteDataSource
) : ContentsRepository {

    private val pref = MySharedPreference.getXAuthToken(context)

    override suspend fun isSeen(contentsId: Int): ContentsHavitResponse {
        return RetrofitObject.provideHavitApi(pref).isHavit(ContentsHavitRequest(contentsId))
    }

    override suspend fun getContentsByCategory(
        categoryId: Int,
        option: String,
        filter: String
    ): List<Contents> {
        return contentsRemoteDataSource.getContentsByCategory(
            categoryId = categoryId,
            option = option,
            filter = filter
        )
    }

    override suspend fun getAllContents(option: String, filter: String): List<Contents> {
        return contentsRemoteDataSource.getAllContents(
            option = option,
            filter = filter
        )
    }

    override suspend fun deleteContents(contentsId: Int): BasicResponse {
        return contentsRemoteDataSource.deleteContents(contentsId)
    }
}
