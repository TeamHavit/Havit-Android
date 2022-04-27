package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.ContentsSearchResponse

interface SearchRepository {

    suspend fun getSearchContents(keyword:String):List<ContentsSearchResponse.Data>

    //suspend fun getSearchCategory()
}