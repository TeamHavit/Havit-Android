package org.sopt.havit.data.repository

import org.sopt.havit.data.source.remote.url.UrlDataSource
import org.sopt.havit.domain.repository.UrlRepository
import javax.inject.Inject

class UrlRepositoryImpl @Inject constructor(
    private val urlDataSource: UrlDataSource,
) : UrlRepository {
    override suspend fun loadOgData(url: String) = urlDataSource.loadOgData(url)
}
