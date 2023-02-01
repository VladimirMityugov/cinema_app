package com.example.skillcinemaapp.presentation.adapters

import android.view.View
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom


class SecondCustomAdapterCommon(
    val onSecondCustomItemClick: (ItemCustom) -> Unit,
    val onShowAllSecondCustomClick: (View) -> Unit
) : FirstCustomAdapterCommon(onSecondCustomItemClick, onShowAllSecondCustomClick)