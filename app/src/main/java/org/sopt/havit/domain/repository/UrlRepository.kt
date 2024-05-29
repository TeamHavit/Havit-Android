package org.sopt.havit.domain.repository

import kotlinx.coroutines.flow.Flow
import org.sopt.havit.data.remote.OgData

fun interface UrlRepository {

    fun loadOgData(url: String): Flow<OgData>
}
