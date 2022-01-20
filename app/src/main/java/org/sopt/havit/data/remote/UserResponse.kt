package org.sopt.havit.data.remote

data class UserResponse(
    val data: UserData,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class UserData(
        val nickname: String,
        val totalCategoryNumber: Int,
        val totalContentNumber: Int,
        val totalSeenContentNumber: Int
    )
}