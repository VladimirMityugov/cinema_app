package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding
import java.util.*


open class FirstCustomAdapterIndividual(
    val onFirstCustomItemClick: (ItemCustom) -> Unit
) :
    PagingDataAdapter<ItemCustom, CustomViewHolderIndividual>(DiffUtilCallBackCustom()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolderIndividual {
        return CustomViewHolderIndividual(
            binding = MovieItemSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFirstCustomItemClick = onFirstCustomItemClick
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolderIndividual, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

class CustomViewHolderIndividual(
    val binding: MovieItemSelectionBinding,
    val onFirstCustomItemClick: (ItemCustom) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ItemCustom) {
        with(binding) {
            Glide
                .with(moviePicture.context)
                .load(item.posterUrlPreview)
                .centerCrop()
                .into(moviePicture)

            movieGenre.text = item.genres.firstOrNull()?.genre

            if(item.watchedStatus !=null){
                watchedStatus.isVisible = item.watchedStatus== true
            } else watchedStatus.isVisible = false

            if (item.nameRu == null && item.nameEn == null && item.nameOriginal == null) {
                movieTitle.isVisible = false
            } else {
                movieTitle.text = when {
                    Locale.getDefault() == Locale.US || Locale.getDefault() == Locale.UK -> item.nameOriginal
                        ?: item.nameEn.toString()
                    else -> item.nameRu
                }
            }

            if (item.ratingKinopoisk == null && item.ratingImdb == null) {
                movieRating.isVisible = false
            } else {
                movieRating.text = when {
                    item.ratingKinopoisk != null -> item.ratingKinopoisk.toString()
                    else -> item.ratingImdb.toString()
                }
            }
binding.root.setOnClickListener {
    onFirstCustomItemClick(item)
}
        }
    }
}


