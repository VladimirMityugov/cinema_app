package com.example.skillcinemaapp.data.remote.movie_info_dto

data class MovieDto(
    val countries: List<Country>?,
    val coverUrl: String?,
    val description: String?,
    val editorAnnotation: String?,
    val endYear: Int?,
    val filmLength: Int?,
    val genres: List<Genre>?,
    val imdbId: String?,
    val kinopoiskId: Int,
    val logoUrl: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val nameRu: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingAgeLimits: String?,
    val ratingAwait: Any?,
    val ratingAwaitCount: Int?,
    val ratingFilmCritics: Double?,
    val ratingFilmCriticsVoteCount: Int?,
    val ratingGoodReview: Double?,
    val ratingGoodReviewVoteCount: Int?,
    val ratingImdb: Double?,
    val ratingImdbVoteCount: Int?,
    val ratingKinopoisk: Double?,
    val ratingKinopoiskVoteCount: Int?,
    val ratingMpaa: String?,
    val ratingRfCritics: Double?,
    val ratingRfCriticsVoteCount: Int?,
    val reviewsCount: Int,
    val shortDescription: String?,
    val shortFilm: Boolean?,
    val slogan: String?,
    val startYear: Int?,
    val serial: Boolean?,
    val webUrl: String,
    val year: Int?
)