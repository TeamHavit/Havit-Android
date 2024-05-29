package org.sopt.havit.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.sopt.havit.HavitFirebaseMessagingService.Companion.TAG
import org.sopt.havit.data.remote.OgData
import org.sopt.havit.domain.repository.UrlRepository
import javax.inject.Inject

class LoadOgDataUseCase @Inject constructor(
    private val urlRepository: UrlRepository,
) {

    fun loadOgData(url: String): Flow<OgData> = flow {
        urlRepository.loadOgData(url)
            .catch { e ->
                Log.e(TAG, "loadOgData: $e")
                emit(OgData.default(url))
            }.collect {
                val processedOgData = it.setOgTitleIfBlank()
                emit(processedOgData)
            }
    }

    operator fun invoke(url: String): Flow<OgData> = loadOgData(url)

}