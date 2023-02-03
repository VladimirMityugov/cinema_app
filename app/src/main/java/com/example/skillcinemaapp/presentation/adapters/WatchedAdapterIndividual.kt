package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding
import java.util.*


class WatchedAdapterIndividual(
    val onWatchedItemClick: (Movie) -> Unit
) :
    ListAdapter<Movie, WatchedHolderIndividual>(DiffUtilCallBackWatched()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedHolderIndividual {
        return WatchedHolderIndividual(
            binding = MovieItemSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onWatchedItemClick = onWatchedItemClick
        )
    }

    override fun onBindViewHolder(holder: WatchedHolderIndividual, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

class WatchedHolderIndividual(
    val binding: MovieItemSelectionBinding,
    val onWatchedItemClick: (Movie) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Movie) {
        with(binding) {
            Glide
                .with(moviePicture.context)
                .load(item.posterUri)
                .centerCrop()
                .into(moviePicture)

            movieGenre.text = item.genre
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



