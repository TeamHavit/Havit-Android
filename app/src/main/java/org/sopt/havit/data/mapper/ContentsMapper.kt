package org.sopt.havit.data.mapper

import org.sopt.havit.data.remote.ContentsSearchResponse
import org.sopt.havit.domain.entity.Contents

object ContentsMapper {


    fun mapperToContents(data: ContentsSearchResponse.Data) =
        Contents.Data(
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