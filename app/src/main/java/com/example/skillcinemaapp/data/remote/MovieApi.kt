package com.example.skillcinemaapp.data.remote

import com.example.skillcinemaapp.data.remote.person_dto.PersonDto
import com.example.skillcinemaapp.data.remote.filters_dto.FiltersDto
import com.example.skillcinemaapp.data.remote.popular_dto.PopularDto
import com.example.skillcinemaapp.data.remote.premiers_dto.PremiersDto
import com.example.skillcinemaapp.data.remote.custom_selection_dto.CustomSelectionDto
import com.example.skillcinemaapp.data.remote.images_dto.ImagesDto
import com.example.skillcinemaapp.data.remote.movie_info_dto.MovieDto
import com.example.skillcinemaapp.data.remote.series_info_dto.SeriesInfoDto
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMoviesDto
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/premieres?")
    suspend fun getPremiers(
        @Query("year") year: Int,
        @Query("month") month: String
    ): PremiersDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun getPopular(
        @Query("page") page: Int
    ): PopularDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun getTop(
        @Query("page") page: Int
    ): PopularDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/filters")
    suspend fun getFilters(): FiltersDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films?countries=1&genres=13&type=TV_SERIES&ratingFrom=7&ratingTo=10&yearFrom=2018&yearTo=2022")
    suspend fun getSeries(
        @Query("page") page: Int
    ): CustomSelectionDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films?type=FILM&ratingFrom=8&ratingTo=10&yearFrom=2010&yearTo=2022")
    suspend fun getFirstCustomPagedSelection(
        @Query ("page") page: Int,
        @Query("countries") countries: Int,
        @Query("genres") genres: Int,
    ): CustomSelectionDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films?type=FILM&ratingFrom=7&ratingTo=10&yearFrom=2008&yearTo=2023")
    suspend fun getSecondCustomPagedSelection(
        @Query ("page") page: Int,
        @Query("countries") countries: Int,
        @Query("genres") genres: Int,
    ): CustomSelectionDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/{id}")
    suspend fun getMovieInfo(
        @Path ("id") movieId: Int
    ): MovieDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v1/staff?")
    suspend fun getStaffInfo(
        @Query ("filmId") filmId: Int
    ): List<StaffDto>

    @Headers("X-API-KEY: $KEY")
    @GET("api/v1/staff/{id}")
    suspend fun getPersonInfo(
        @Path("id") personId: Int
    ): PersonDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/{id}/images?")
    suspend fun getImages(
        @Path("id") id: Int,
        @Query ("page") page: Int?,
        @Query ("type") type: String?
    ): ImagesDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/{id}/similars")
    suspend fun getSimilarMovies(
        @Path("id") id: Int
    ): SimilarMoviesDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films/{id}/seasons")
    suspend fun getSeriesInfo(
        @Path("id") id: Int
    ): SeriesInfoDto

    @Headers("X-API-KEY: $KEY")
    @GET("api/v2.2/films?")
    suspend fun getSearchResult(
        @Query("countries") countries: Int,
        @Query("genres") genres: Int,
        @Query("order") order: String,
        @Query("type") type: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String?,
        @Query("page") page: Int
    ): CustomSelectionDto


    companion object {
        const val BASE_URl = "https://kinopoiskapiunofficial.tech/"
        private const val KEY = "d8478a49-9e65-4bdc-8b9b-81a7b79e1c4c"
    }
}