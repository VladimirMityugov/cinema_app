package com.example.skillcinemaapp.presentation.paging_sources


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.domain.UseCaseRemote
import javax.inject.Inject



class ImagesPagingSource @Inject constructor
    (
    private val useCaseRemote: UseCaseRemote,
    val movieId: Int,
    val type: String?
) : PagingSource<Int, Image>() {

    override fun getRefreshKey(state: PagingState<Int, Image>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            useCaseRemote.getImages(movieId, page, type)
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
