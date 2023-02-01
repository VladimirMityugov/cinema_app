package com.example.skillcinemaapp.data.remote.similar_movies

data class SimilarMoviesDto(
    val items: List<SimilarMovie>,
    val total: Int
)