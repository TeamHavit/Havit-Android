package org.sopt.havit.data.mapper

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.domain.entity.Contents
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ContentsMapper @Inject constructor() {

    @Provides
    @Singleton
    fun toContents(data: ContentsSearchResponse.Data) =
        Contents(
            createdAt = data.createdAt,
            description = data.description,
            id = data.id,
            image = data.image,
            isNotified = data.isNotified,
            isSeen = data.isSeen,
            notificationTime = data.notificationTime,
            title = data.title,
            url = data.url
        )
}
