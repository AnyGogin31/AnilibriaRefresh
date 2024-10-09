package com.xbot.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.xbot.data.datasource.TitleDataSource.Companion.PAGE_SIZE
import com.xbot.domain.model.TitleModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TitlePagingSource @Inject constructor(
    private val dataSource: TitleDataSource
) : PagingSource<Int, TitleModel>() {

    override val jumpingSupported: Boolean = true

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TitleModel> {
        val pageIndex = params.key ?: 0
        val loadSize = params.loadSize
        return try {
            val page = dataSource.getTitleUpdates(pageIndex + 1, loadSize).first()

            Log.e("PagingSource", "$pageIndex")

            val newCount = page.items.size
            val total = page.total
            val itemsBefore = pageIndex * PAGE_SIZE
            val itemsAfter = total - (itemsBefore + newCount)

            val prevKey = if (pageIndex == 0) null else pageIndex - 1
            val nextKey = if (itemsAfter == 0) null else pageIndex + 1

            LoadResult.Page(
                data = page.items,
                prevKey = prevKey,
                nextKey = nextKey,
                itemsBefore = itemsBefore,
                itemsAfter = itemsAfter
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TitleModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val pageIndex = anchorPosition / PAGE_SIZE
        return pageIndex
    }
}