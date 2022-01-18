package org.sopt.havit.data.repository

import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsHavitResponse

interface ContentsRepository {

    suspend fun isSeen(contentsId:Int):ContentsHavitResponse
}