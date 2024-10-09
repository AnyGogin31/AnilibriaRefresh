package com.xbot.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.xbot.data.datasource.TitleDataSource
import com.xbot.data.datasource.TitleDataSource.Companion.PAGE_SIZE
import com.xbot.data.datasource.TitlePagingSource
import com.xbot.domain.model.TitleModel
import com.xbot.domain.repository.TitleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    private val dataSource: TitleDataSource
) : TitleRepository {
    override fun getLatestTitles(): Flow<PagingData<TitleModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE,
                enablePlaceholders = true,
                jumpThreshold = PAGE_SIZE * 3
            ),
            pagingSourceFactory = { TitlePagingSource(dataSource) }
        ).flow
    }
}