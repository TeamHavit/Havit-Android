package org.sopt.havit.domain.repository

import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.data.remote.SearchContentsResponse
import org.sopt.havit.data.repository.SearchRepository

class SearchRepositoryImpl :SearchRepository {

    override suspend fun getSearchContents(keyword:String): List<ContentsSearchResponse.Data> =
        RetrofitObject.provideHavitApi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiaWRGaXJlYmFzZSI6IiIsImlhdCI6MTY0MjEzOTgwMCwiZXhwIjoxNjQ0NzMxODAwLCJpc3MiOiJoYXZpdCJ9.-VsZ4c5mU96GRwGSLjf-hSiU8HD-LVK8V3a5UszUAWk")
            .getSearchContents(keyword).data

}