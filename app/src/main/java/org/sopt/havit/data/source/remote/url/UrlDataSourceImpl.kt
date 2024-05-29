package org.sopt.havit.data.source.remote.url

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.sopt.havit.data.remote.OgData
import javax.inject.Inject

class UrlDataSourceImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
) : UrlDataSource {

    override suspend fun loadOgData(url: String): OgData = withContext(dispatcher) {
        val doc = Jsoup.connect(url).get()
        val title = doc.select("meta[property=og:title]").attr("content").ifEmpty { doc.title() }
        val description = doc.select("meta[property=og:description]").attr("content")
        val imageUrl = doc.select("meta[property=og:image]").attr("content")
        OgData(ogTitle = title, ogDescription = description, ogUrl = url, ogImage = imageUrl)
    }
}
