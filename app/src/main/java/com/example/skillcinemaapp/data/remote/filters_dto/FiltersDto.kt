package com.example.skillcinemaapp.data.remote.filters_dto

data class FiltersDto(
    val countries: List<CountryFilters>,
    val genres: List<GenreFilters>
)