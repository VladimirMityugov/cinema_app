package com.example.skillcinemaapp.presentation.adapters

import android.view.View
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom

class SeriesAdapterCommon(
    val onSeriesItemClick: (ItemCustom) -> Unit,
    val onShowAllSeriesClick: (View) -> Unit
): FirstCustomAdapterCommon(onSeriesItemClick, onShowAllSeriesClick)
