package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.ContentsHavitResponse

interface ContentsRepository {

    suspend fun isSeen(contentsId: Int): ContentsHavitResponse
}