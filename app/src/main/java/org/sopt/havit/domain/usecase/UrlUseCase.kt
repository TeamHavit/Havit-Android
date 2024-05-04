package org.sopt.havit.domain.usecase

import org.sopt.havit.data.remote.OgData
import org.sopt.havit.domain.repository.UrlRepository
import javax.inject.Inject

class UrlUseCase @Inject constructor(
    private val urlRepository: UrlRepository,
) {

    suspend fun loadOgData(url: String): OgData {
        return urlRepository.loadOgData(url)//.setOgTitleIfBlank()
    }

    private fun OgData.setOgTitleIfBlank(): OgData {
        return this.apply {
            if (ogTitle.isBlank()) ogTitle = NO_TITLE_CONTENTS
        }
    }

    companion object {
        const val NO_TITLE_CONTENTS = "제목 없는 콘텐츠"
    }

}