package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.custom_selection_dto.ItemCustom
import com.example.skillcinemaapp.databinding.MovieItemBinding
import com.example.skillcinemaapp.databinding.ShowAllItemBinding



open class FirstCustomAdapterCommon (
    val onFirstCustomItemClick: (ItemCustom) -> Unit,
    val onShowAllFirstCustomClick: (View) -> Unit
) :
    ListAdapter<ItemCustom, RecyclerView.ViewHolder>(DiffUtilCallBackCustom()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_MOVIES) {
            CustomSelectionViewHolderCommon(
                binding = MovieItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ),
                onItemClick = onFirstCustomItemClick
            )
        } else ShowAllViewHolder(
            binding = ShowAllItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            onShowAllItemClick = onShowAllFirstCustomClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            if (getItemViewType(position) == ITEM_MOVIES) {
                (holder as CustomSelectionViewHolderCommon).bind(it)
            } else (holder as ShowAllViewHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 20) ITEM_SHOW_ALL
        else ITEM_MOVIES
    }

    companion object {
        private const val ITEM_MOVIES = 0
        private const val ITEM_SHOW_ALL = 1
    }


}

class ShowAllViewHolder(
    val binding: ShowAllItemBinding,
    val onShowAllItemClick: (View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        with(binding) {
            arrowPicture.setOnClickListener {
                onShowAllItemClick(it)
            }
        }
    }
}

class CustomSelectionViewHolderCommon(
    val binding: MovieItemBinding,
    val onItemClick: (ItemCustom) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
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
                    item.nameRu != null -> item.nameRu
                    item.nameEn != null -> item.nameEn.toString()
                    else -> item.nameOriginal
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

        }
        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}

class DiffUtilCallBackCustom : DiffUtil.ItemCallback<ItemCustom>() {
    override fun areItemsTheSame(oldItem: ItemCustom, newItem: ItemCustom): Boolean {
        return oldItem.kinopoiskId == newItem.kinopoiskId
    }

    override fun areContentsTheSame(oldItem: ItemCustom, newItem: ItemCustom): Boolean {
        return oldItem == newItem
    }
}

