package org.sopt.havit.data.repository

import org.sopt.havit.data.remote.SearchContentsResponse

interface SearchRepository {

    suspend fun getSearchContents(keyword:String):List<SearchContentsResponse.Contents>

    //suspend fun getSearchCategory()
}