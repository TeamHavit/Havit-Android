package org.sopt.havit.data.repository

import android.content.Context
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.domain.repository.SearchRepository
import org.sopt.havit.util.MySharedPreference

class SearchRepositoryImpl(context: Context) : SearchRepository {

    private val pref = MySharedPreference.getXAuthToken(context)

    override suspend fun getSearchContents(keyword:String): List<ContentsSearchResponse.Data> =
        RetrofitObject.provideHavitApi(pref)
            .getSearchContents(keyword).data

}