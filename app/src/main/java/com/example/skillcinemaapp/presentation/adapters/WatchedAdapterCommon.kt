package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.ClearHistoryItemBinding
import com.example.skillcinemaapp.databinding.MovieItemBinding
import java.util.*


open class WatchedAdapterCommon(
    val onWatchedItemClick: (Movie) -> Unit,
    val onClearHistoryClick: (View) -> Unit
) :
    ListAdapter<Movie, RecyclerView.ViewHolder>(DiffUtilCallBackWatched()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_MOVIES) {
            WatchedViewHolderCommon(
                binding = MovieItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ),
                onWatchedItemClick = onWatchedItemClick
            )
        } else ClearHistoryViewHolder(
            binding = ClearHistoryItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            onClearHistoryClick = onClearHistoryClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            if (getItemViewType(position) == ITEM_MOVIES) {
                (holder as WatchedViewHolderCommon).bind(it)
            } else (holder as ClearHistoryViewHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 10) ITEM_CLEAR_HISTORY
        else ITEM_MOVIES
    }

    companion object {
        private const val ITEM_MOVIES = 0
        private const val ITEM_CLEAR_HISTORY = 1
    }
}

class ClearHistoryViewHolder(
    val binding: ClearHistoryItemBinding,
    val onClearHistoryClick: (View) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind() {
        with(binding) {
            trashPicture.setOnClickListener {
                onClearHistoryClick(it)
            }
        }
    }
}

class WatchedViewHolderCommon(
    val binding: MovieItemBinding,
    val onWatchedItemClick: (Movie) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Movie) {
        with(binding) {
            Glide
                .with(moviePicture.context)
                .load(item.posterUri)
                .centerCrop()
                .into(moviePicture)

            movieGenre.text = item.genre ?: ""

            movieTitle.text = when {
                Locale.getDefault() == Locale.US || Locale.getDefault() == Locale.UK -> item.nameEn ?: ""
                else -> item.movieName ?: ""
            }

            movieRating.text = item.rating.toString()
        }

        binding.root.setOnClickListener {
            onWatchedItemClick(item)
        }
    }
}


class DiffUtilCallBackWatched : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}

