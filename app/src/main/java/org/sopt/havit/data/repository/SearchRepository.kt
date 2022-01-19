package org.sopt.havit.data.repository

import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.data.remote.SearchContentsResponse

interface SearchRepository {

    suspend fun getSearchContents(keyword:String):List<ContentsSearchResponse.Data>

    //suspend fun getSearchCategory()
}