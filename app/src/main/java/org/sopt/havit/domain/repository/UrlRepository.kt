package org.sopt.havit.domain.repository

import org.sopt.havit.data.remote.OgData

interface UrlRepository {

    suspend fun loadOgData(url: String): OgData
}
