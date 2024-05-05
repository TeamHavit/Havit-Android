package org.sopt.havit.data.source.remote.community

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.sopt.havit.data.api.HavitApi
import org.sopt.havit.domain.entity.CommunityPost
import javax.inject.Inject

class CommunityPagingSource @Inject constructor(
    private val havitApi: HavitApi
) : PagingSource<Int, CommunityPost>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommunityPost> {
        val page = params.key ?: 1
        return try {
            val response = havitApi.getCommunityAllPosts(
                page = page,
                limit = PAGE_LIMIT
            ).data

            val items = response?.posts

            LoadResult.Page(
                data = items!!,
                prevKey = if (page <= 1) null else (page - 1),
                nextKey = if (response.isLastPage) null else (page + 1)
            )
        } catch (e: Exception) {
            Log.e("CommunityPagingSource", "error : $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CommunityPost>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey
        }
    }

    companion object {
        private const val PAGE_LIMIT = 20
    }
}