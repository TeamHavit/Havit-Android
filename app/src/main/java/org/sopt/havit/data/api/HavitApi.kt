package org.sopt.havit.data.api

import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsHavitResponse
import org.sopt.havit.data.remote.SearchContentsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface HavitApi {

    @GET("content/search/?keyword=")
    suspend fun getSearchContents(
        @Query("keyword") keyword: String
    ): SearchContentsResponse

    @GET("content/search?categoryId=&keyword=")
    suspend fun getSearchCategory(
        @Query("categoryId") categoryId: String,
        @Query("keyword") keyword: String
    ): SearchContentsResponse

    @PATCH("content/check")
    suspend fun isHavit(
        @Body body: ContentsHavitRequest
    ): ContentsHavitResponse

    @GET("category")
    suspend fun getAllCategory(): CategoryResponse
}