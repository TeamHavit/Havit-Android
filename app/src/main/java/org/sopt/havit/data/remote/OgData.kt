package org.sopt.havit.data.remote

const val NO_TITLE_CONTENTS = "제목 없는 콘텐츠"

data class OgData(
    var ogTitle: String = "",
    var ogDescription: String = "",
    var ogUrl: String = "",
    var ogImage: String? = "",
) {
    companion object {
        fun default(url: String) = OgData(ogTitle = NO_TITLE_CONTENTS, ogUrl = url)
    }

    fun setOgTitleIfBlank(): OgData {
        return this.apply {
            if (ogTitle.isBlank()) ogTitle = NO_TITLE_CONTENTS
        }
    }
}
