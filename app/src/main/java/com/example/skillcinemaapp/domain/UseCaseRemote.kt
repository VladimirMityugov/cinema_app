package com.example.skillcinemaapp.domain

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
import com.example.skillcinemaapp.data.repositories.RepositoryNetwork
import javax.inject.Inject


class UseCaseRemote @Inject constructor(private val repositoryNetwork: RepositoryNetwork) {

    suspend fun getPremiers(year: Int, month: String): List<Item> {
        return repositoryNetwork.getPremiers(year, month)
    }

    suspend fun getPopular(page: Int): List<Film> {
        return repositoryNetwork.getPopular(page)
    }

    suspend fun getTop(page: Int): List<Film> {
        return repositoryNetwork.getTop(page)
    }

    suspend fun getFilters(): FiltersDto {
        return repositoryNetwork.getFilters()
    }

    suspend fun getSeries(page: Int): List<ItemCustom> {
        return repositoryNetwork.getSeries(page)
    }

    suspend fun getFirstCustomPagedSelection(
        page: Int,
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return repositoryNetwork.getFirstCustomPagedSelection(page, country, genre)
    }

    suspend fun getSecondCustomPagedSelection(
        page: Int,
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return repositoryNetwork.getSecondCustomPagedSelection(page, country, genre)
    }

    suspend fun getFirstCustomListSelection(
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return repositoryNetwork.getFirstCustomListSelection(country, genre)
    }

    suspend fun getSecondCustomListSelection(
        country: Int,
        genre: Int
    ): List<ItemCustom> {
        return repositoryNetwork.getSecondCustomListSelection(country, genre)
    }

    suspend fun getSeriesListSelection(): List<ItemCustom> {
        return repositoryNetwork.getSeriesListSelection()
    }

    suspend fun getPopularListSelection(): List<Film> {
        return repositoryNetwork.getPopularListSelection()
    }

    suspend fun getTopListSelection(): List<Film> {
        return repositoryNetwork.getTopListSelection()
    }

    suspend fun getMovieInfo(movieId: Int): MovieDto {
        return repositoryNetwork.getMovieInfo(movieId)
    }

    suspend fun getStaffInfo(filmId: Int): List<StaffDto> {
        return repositoryNetwork.getStaffInfo(filmId)
    }

    suspend fun getPersonInfo(personId: Int): PersonDto {
        return repositoryNetwork.getPersonInfo(personId)
    }

    suspend fun getImages(page: Int, movieId: Int, type: String?): List<Image> {
        return repositoryNetwork.getImages(page, movieId, type)
    }

    suspend fun getImagesList(movieId: Int): ImagesDto {
        return repositoryNetwork.getImagesList(movieId)
    }

    suspend fun getSimilarMovies(movieId: Int): List<SimilarMovie> {
        return repositoryNetwork.getSimilarMovies(movieId)
    }

    suspend fun getSeriesInfo(seriesId: Int): SeriesInfoDto {
        return repositoryNetwork.getSeriesInfo(seriesId)
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
        return repositoryNetwork.getSearchResult(countries, genres, order, type, ratingFrom, ratingTo, yearFrom, yearTo, keyword, page)
    }

}