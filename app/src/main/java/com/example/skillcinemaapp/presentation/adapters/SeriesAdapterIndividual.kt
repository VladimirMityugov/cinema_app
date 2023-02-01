package com.example.skillcinemaapp.presentation.adapters

import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom

class SeriesAdapterIndividual(
    val onSeriesItemClick: (ItemCustom) -> Unit,
    ): FirstCustomAdapterIndividual(onSeriesItemClick)
