package org.sopt.havit.data.repository

import kotlinx.coroutines.flow.flow
import org.sopt.havit.data.source.remote.url.UrlDataSource
import org.sopt.havit.domain.repository.UrlRepository
import javax.inject.Inject

class UrlRepositoryImpl @Inject constructor(
    private val urlDataSource: UrlDataSource,
) : UrlRepository {
    override fun loadOgData(url: String) = flow {
        emit(urlDataSource.loadOgData(url))
    }
}
