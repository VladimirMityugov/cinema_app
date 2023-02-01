package com.example.skillcinemaapp.presentation.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.domain.UseCaseRemote
import javax.inject.Inject

class SearchPagingSource @Inject constructor
    (
    private val useCaseRemote: UseCaseRemote,
    private val countries: Int,
    private val genres: Int,
    private val order: String,
    private val type: String,
    private val ratingFrom: Int,
    private val ratingTo: Int,
    private val yearFrom: Int,
    private val yearTo: Int,
    private val keyword: String
) : PagingSource<Int, ItemCustom>() {

    override fun getRefreshKey(state: PagingState<Int, ItemCustom>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemCustom> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            useCaseRemote.getSearchResult(
                countries = countries,
                genres = genres,
                order = order,
                type = type,
                ratingFrom = ratingFrom ,
                ratingTo = ratingTo,
                yearFrom = yearFrom,
                yearTo = yearTo,
                keyword = keyword,
                page
            ).items
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
