package com.example.skillcinemaapp.presentation.adapters

import android.view.View
import com.example.skillcinemaapp.data.remote.popular_dto.Film


class PopularAdapterCommon(
    val onPopularItemClick: (Film) -> Unit,
    val onShowAllPopularClick: (View) -> Unit
) : TopAdapterCommon(onPopularItemClick, onShowAllPopularClick)



