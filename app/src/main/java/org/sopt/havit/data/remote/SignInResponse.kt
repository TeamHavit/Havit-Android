package org.sopt.havit.data.remote

data class SignInResponse(
    val data: Data,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    data class Data(
        val accessToken: String?,
        val firebaseAuthToken: String?,
        val isAlreadyUser: Boolean?,
        val nickname: String?,
        val refreshToken: String?
    )
}