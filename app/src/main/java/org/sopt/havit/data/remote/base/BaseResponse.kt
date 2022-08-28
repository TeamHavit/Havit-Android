package org.sopt.havit.data.remote.base

data class BaseResponse<T>(
    val data: T?,
    val message: String,
    val status: Int,
    val success: Boolean
)
