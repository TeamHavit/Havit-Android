package org.sopt.havit.data.source.remote.url

import org.sopt.havit.data.remote.OgData

interface UrlDataSource {
    suspend fun loadOgData(url: String): OgData

}
