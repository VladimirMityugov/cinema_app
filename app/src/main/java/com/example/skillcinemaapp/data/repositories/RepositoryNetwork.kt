package com.example.skillcinemaapp.data.repositories


import com.example.skillcinemaapp.data.remote.MovieApi
import com.example.skillcinemaapp.data.remote.custom_selection_dto.CustomSelectionDto
import com.example.skillcinemaapp.data.remote.person_dto.PersonDto
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.data.remote.filters_dto.FiltersDto
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.data.remote.images_dto.ImagesDto
import com.example.skillcinemaapp.data.remote.movie_info_dto.MovieDto
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.data.remote.premiers_dto.Item
import com.example.skillcinemaapp.data.remote.series_info_dto.SeriesInfoDto
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import javax.inject.Inject


class RepositoryNetwork @Inject constructor(
    private val movieApi: MovieApi
) {
    suspend fun getPremiers(year: Int, month: String): List<Item> {
        return movieApi.getPremiers(year, month).items
    }

    suspend fun getPopular(page: Int): List<Film> {
        return movieApi.getPopular(page).films
    }

    suspend fun getTop(page: Int): List<Film> {
        return movieApi.getTop(page).films
    }

    suspend fun getFilters(): FiltersDto {
        return movieApi.getFilters()
    }

    suspend fun getSeries(page: Int): List<ItemCustom> {
        return movieApi.getSeries(page).items
    }

    suspend fun getSeriesListSelection(): List<ItemCustom> {
        val moviesList = mutableListOf<ItemCustom>()
        val result = movieApi.getSeries(page = 1)
        if (result.totalPages > 1) {
            moviesList.addAll(result.items)
            val secondPage = movieApi.getSeries(page = 2)
            moviesList.addAll(secondPage.items)
        } else {
            moviesList.addAll(result.items)
        }
        return moviesList
    }

    suspend fun getPopularListSelection(): List<Film> {
        val moviesList = mutableListOf<Film>()
        val result = movieApi.getPopular(page = 1)
        if (result.pagesCount > 1) {
            moviesList.addAll(result.films)
            val secondPage = movieApi.getPopular(page = 2)
            moviesList.addAll(secondPage.films)
        } else {
            moviesList.addAll(result.films)
        }
        return moviesList
    }

    suspend fun getTopListSelection(): List<Film> {
        val moviesList = mutableListOf<Film>()
        val result = movieApi.getTop(page = 1)
        if (result.pagesCount > 1) {
            moviesList.addAll(result.films)
            val secondPage = movieApi.getTop(page = 2)
            moviesList.addAll(secondPage.films)
        } else {
            moviesList.addAll(result.films)
        }
        return moviesList
    }

    suspend fun getFirstCustomListSelection(
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        val moviesList = mutableListOf<ItemCustom>()
        val result = movieApi.getFirstCustomPagedSelection(page = 1, country, genre)
        if (result.totalPages > 1) {
            moviesList.addAll(result.items)
            val secondPage = movieApi.getFirstCustomPagedSelection(page = 2, country, genre)
            moviesList.addAll(secondPage.items)
        } else {
            moviesList.addAll(result.items)
        }
        return moviesList
    }

    suspend fun getSecondCustomListSelection(
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        val moviesList = mutableListOf<ItemCustom>()
        val result = movieApi.getSecondCustomPagedSelection(page = 1, country, genre)
        if (result.totalPages > 1) {
            moviesList.addAll(result.items)
            val secondPage = movieApi.getSecondCustomPagedSelection(page = 2, country, genre)
            moviesList.addAll(secondPage.items)
        } else {
            moviesList.addAll(result.items)
        }
        return moviesList
    }

    suspend fun getFirstCustomPagedSelection(
        page: Int,
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return movieApi.getFirstCustomPagedSelection(page, country, genre).items
    }

    suspend fun getSecondCustomPagedSelection(
        page: Int,
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return movieApi.getSecondCustomPagedSelection(page, country, genre).items
    }

    suspend fun getMovieInfo(movieId: Int): MovieDto {
        return movieApi.getMovieInfo(movieId)
    }

    suspend fun getStaffInfo(filmId: Int): List<StaffDto> {
        return movieApi.getStaffInfo(filmId)
    }

    suspend fun getPersonInfo(personId: Int): PersonDto {
        return movieApi.getPersonInfo(personId)
    }

    suspend fun getImages(movieId: Int, page: Int, type: String?): List<Image> {
        return movieApi.getImages(id = movieId, page = page, type = type).items
    }

    suspend fun getImagesList(movieId: Int): ImagesDto {
        return movieApi.getImages(id = movieId, null, null)
    }

    suspend fun getSimilarMovies(movieId: Int): List<SimilarMovie> {
        return movieApi.getSimilarMovies(id = movieId).items
    }

    suspend fun getSeriesInfo(seriesId: Int): SeriesInfoDto {
        return movieApi.getSeriesInfo(seriesId)
    }

    suspend fun getSearchResult(
        countries: Int,
        genres: Int,
        order: String,
        type: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keyword:String,
        page: Int
    ): CustomSelectionDto {
        return movieApi.getSearchResult(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
    }

}