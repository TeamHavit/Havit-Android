package org.sopt.havit.data.repository

import org.sopt.havit.data.ContentsData

interface SearchRepository {

    suspend fun getSearchContents():List<ContentsData>

    //suspend fun getSearchCategory()
}