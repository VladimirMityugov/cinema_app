package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.databinding.MovieItemBinding
import java.util.*


class SimilarAdapterCommon(
    val onSimilarItemClick: (SimilarMovie) -> Unit
) : ListAdapter<SimilarMovie, SimilarViewHolderCommon>(SimilarDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolderCommon {
        return SimilarViewHolderCommon(
            binding = MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onSimilarItemClick = onSimilarItemClick
        )
    }

    override fun onBindViewHolder(holder: SimilarViewHolderCommon, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class SimilarViewHolderCommon(
    val binding: MovieItemBinding,
    val onSimilarItemClick: (SimilarMovie) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SimilarMovie) {
        with(binding) {

            Glide
                .with(moviePicture.context)
                .load(item.posterUrl)
                .centerCrop()
                .into(moviePicture)

            movieGenre.text = ""
            movieTitle.text = when {
                Locale.getDefault() == Locale.US || Locale.getDefault() == Locale.UK -> item.nameEn
                    ?: item.nameOriginal ?: ""
                else -> item.nameRu ?: ""
            }
            movieRating.isVisible = false

            binding.root.setOnClickListener {
                onSimilarItemClick(item)
            }
        }
    }
}


class SimilarDiffUtilCallBack : DiffUtil.ItemCallback<SimilarMovie>() {
    override fun areItemsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(oldItem: SimilarMovie, newItem: SimilarMovie): Boolean {
        return oldItem == newItem
    }
}
