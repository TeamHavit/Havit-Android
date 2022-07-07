package org.sopt.havit.data.source.remote.contents

import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.domain.entity.Contents
import javax.inject.Inject

class ContentsRemoteDataSourceImpl @Inject constructor(
    private val havitApi: HavitApi
) : ContentsRemoteDataSource {

    override suspend fun getContentsByCategory(
        categoryId: Int,
        option: String,
        filter: String
    ): List<Contents> {
        return havitApi.getCategoryContents(
            categoryId = categoryId,
            option = option,
            filter = filter
        ).data ?: emptyList()
    }

    override suspend fun getAllContents(option: String, filter: String): List<Contents> {
        return havitApi.getAllContents(
            option = option,
            filter = filter
        ).data ?: emptyList()
    }

    override suspend fun deleteContents(contentsId: Int): BasicResponse {
        return havitApi.deleteContents(contentsId)
    }

}
