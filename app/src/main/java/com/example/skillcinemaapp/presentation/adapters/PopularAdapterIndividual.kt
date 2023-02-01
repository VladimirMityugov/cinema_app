package com.example.skillcinemaapp.presentation.adapters

import com.example.skillcinemaapp.data.remote.popular_dto.Film


class PopularAdapterIndividual(
    val onPopularItemClick: (Film) -> Unit
) : TopAdapterIndividual(onPopularItemClick)



