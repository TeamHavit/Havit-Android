package org.sopt.havit.data.api

import org.sopt.havit.data.remote.*
import org.sopt.havit.data.remote.base.BaseResponse
import org.sopt.havit.domain.entity.Category
import org.sopt.havit.domain.entity.Contents
import org.sopt.havit.domain.entity.Notice
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
    suspend fun getAllCategories(): BaseResponse<List<Category>>

    @GET("content/{contentId}/category")
    suspend fun getAllCategoryList(
        @Path("contentId") contentId: Int
    ): AllCategoryResponse

    @GET("category")
    suspend fun getCategoryList(): CategoryResponse

    @POST("category")
    suspend fun addCategory(
        @Body body: CategoryAddRequest
    ): BasicResponse

    @POST("content")
    suspend fun createContents(
        @Body body: CreateContentsRequest
    ): CreateContentsResponse

    @GET("category/{categoryId}?option=&filter=")
    suspend fun getCategoryContents(
        @Path("categoryId") categoryId: Int,
        @Query("option") option: String,
        @Query("filter") filter: String
    ): BaseResponse<List<Contents>>

    @GET("content/recent")
    suspend fun getContentsRecent(): ContentsSimpleResponse

    @GET("content/unseen")
    suspend fun getContentsUnseen(): ContentsSimpleResponse

    @DELETE("category/{categoryId}")
    suspend fun deleteCategory(
        @Path("categoryId") categoryId: Int
    ): BasicResponse

    @GET("recommendation")
    suspend fun getRecommendation(): RecommendationResponse

    @PATCH("category/order")
    suspend fun updateCategoryOrder(
        @Body body: UpdateCategoriesOrderRequest
    ): BasicResponse

    @PATCH("category/{categoryId}")
    suspend fun updateCategoryInfo(
        @Path("categoryId") categoryId: Int,
        @Body body: UpdateCategoryInfoRequest
    ): BasicResponse

    @GET("content?option=&filter=")
    suspend fun getAllContents(
        @Query("option") option: String,
        @Query("filter") filter: String
    ): BaseResponse<List<Contents>>

    @GET("user")
    suspend fun getUserData(): UserResponse

    @DELETE("content/{contentId}")
    suspend fun deleteContents(
        @Path("contentId") contentId: Int
    ): BasicResponse

    @PATCH("content/title/{contentId}")
    suspend fun modifyContentTitle(
        @Path("contentId") contentId: Int,
        @Body body: ModifyTitleParams
    ): BasicResponse

    @PATCH("user")
    suspend fun modifyUserNickname(
        @Body body: NewNicknameRequest
    ): BasicResponse

    @POST("auth/signin")
    suspend fun postSignIn(
        @Body body: SignInRequest
    ): SignInResponse

    @POST("auth/signup")
    suspend fun postSignUp(
        @Body body: SignUpRequest
    ): SignUpResponse

    @GET("content/notification?option=")
    suspend fun getNotification(
        @Query("option") option: String,
    ): NotificationResponse

    @PATCH("content/category")
    suspend fun modifyContentCategory(
        @Body body: ModifyContentCategoryParams
    ): BasicResponse

    @DELETE("auth/user")
    suspend fun deleteUser()

    @PATCH("content/{contentId}/notification")
    suspend fun modifyContentNotification(
        @Path("contentId") contentId: Int,
        @Body notificationTime: ModifyContentNotificationParams
    ): BasicResponse

    @DELETE("content/{contentId}/notification")
    suspend fun deleteContentNotification(
        @Path("contentId") contentId: Int
    ): BasicResponse

    @PUT("user/fcm-token")
    suspend fun refreshFcmToken(
        @Body body: FcmTokenParams
    ): BasicResponse

    @GET("notice")
    suspend fun getNoticeList(): BaseResponse<List<Notice>>
}
