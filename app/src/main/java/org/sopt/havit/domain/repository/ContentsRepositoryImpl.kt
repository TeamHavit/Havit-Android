package org.sopt.havit.domain.repository

import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsHavitRequest
import org.sopt.havit.data.remote.ContentsHavitResponse
import org.sopt.havit.data.repository.ContentsRepository

class ContentsRepositoryImpl :ContentsRepository {

    override suspend fun isSeen(contentsId: Int): ContentsHavitResponse {
        return RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk").isHavit(ContentsHavitRequest(contentsId))
    }
}