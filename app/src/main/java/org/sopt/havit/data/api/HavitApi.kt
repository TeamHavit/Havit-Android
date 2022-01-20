package org.sopt.havit.data.api

import org.sopt.havit.data.remote.*
import retrofit2.http.*

interface HavitApi {

    @GET("content/search/?keyword=")
    suspend fun getSearchContents(
        @Query("keyword") keyword: String
    ): ContentsSearchResponse

    @GET("content/search?categoryId=&keyword=")
    suspend fun getSearchCategory(
        @Query("categoryId") categoryId: String,
        @Query("keyword") keyword: String
    ): ContentsSearchResponse

    @PATCH("content/check")
    suspend fun isHavit(
        @Body body: ContentsHavitRequest
    ): ContentsHavitResponse

    @GET("category")
    suspend fun getAllCategory(): CategoryResponse

    @GET("category")
    suspend fun getCategoryNum(): CategoryResponse

    @GET("category/{categoryId}?option=&filter=")
    suspend fun getCategoryContents(
        @Path("categoryId") categoryId: Int,
        @Query("option") option: String,
        @Query("filter") filter: String
    ): ContentsResponse

    @GET("content/recent")
    suspend fun getContentsRecent(): ContentsSimpleResponse

    @GET("content/unseen")
    suspend fun getContentsUnseen(): ContentsSimpleResponse

    @GET("category/{categoryId}")
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Int
    ): ContentsResponse

    @GET("recommendation")
    suspend fun getRecommendation(): RecommendationResponse

}