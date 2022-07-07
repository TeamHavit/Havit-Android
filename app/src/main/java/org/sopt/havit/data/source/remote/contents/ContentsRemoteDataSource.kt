package org.sopt.havit.data.source.remote.contents

import org.sopt.havit.data.remote.BasicResponse
import org.sopt.havit.domain.entity.Contents

interface ContentsRemoteDataSource {

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
