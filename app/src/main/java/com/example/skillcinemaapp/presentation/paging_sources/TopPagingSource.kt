package com.example.skillcinemaapp.presentation.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.domain.UseCaseRemote
import javax.inject.Inject

class TopPagingSource @Inject constructor
    (
    private val useCaseRemote: UseCaseRemote
) : PagingSource<Int, Film>() {

    override fun getRefreshKey(state: PagingState<Int, Film>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            useCaseRemote.getTop(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(throwable = Throwable(message = "There is no data to indicate")) }
        )
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}
