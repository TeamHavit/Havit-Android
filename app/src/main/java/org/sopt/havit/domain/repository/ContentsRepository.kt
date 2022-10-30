package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.data.remote.ContentsHavitResponse
import org.sopt.havit.domain.entity.Contents

interface ContentsRepository {

    suspend fun isSeen(contentsId: Int): ContentsHavitResponse

    suspend fun getContentsByCategory(
        categoryId: Int,
        option: String,
        filter: String
    ): List<Contents>

    suspend fun getAllContents(
        option: String,
        filter: String
    ): List<Contents>

    suspend fun deleteContents(contentsId: Int): BasicResponse
}
