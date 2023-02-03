package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.local.entities.Movie
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding
import java.util.*


class InterestingAdapterIndividual(
    val onInterestingItemClick: (Movie) -> Unit
) :
    ListAdapter<Movie, InterestingHolderIndividual>(DiffUtilCallBackWatched()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestingHolderIndividual {
        return InterestingHolderIndividual(
            binding = MovieItemSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onInterestingItemClick = onInterestingItemClick
        )
    }

    override fun onBindViewHolder(holder: InterestingHolderIndividual, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

class InterestingHolderIndividual(
    val binding: MovieItemSelectionBinding,
    val onInterestingItemClick: (Movie) -> Unit
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
                Locale.getDefault() == Locale("ru", "RU")-> item.movieName
                else -> item.nameEn
            }
            movieRating.text = item.rating.toString()
        }
        binding.root.setOnClickListener {
            onInterestingItemClick(item)
        }
    }
}



