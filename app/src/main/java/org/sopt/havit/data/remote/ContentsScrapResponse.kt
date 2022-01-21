package org.sopt.havit.data.remote

import org.sopt.havit.data.ContentsSummeryData

data class ContentsScrapResponse(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: ContentsSummeryData
)