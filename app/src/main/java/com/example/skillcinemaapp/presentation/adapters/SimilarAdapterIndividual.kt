package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.similar_movies.SimilarMovie
import com.example.skillcinemaapp.databinding.MovieItemBinding
import java.util.*


class SimilarAdapterIndividual(
    val onSimilarItemClick: (SimilarMovie) -> Unit
    ) : ListAdapter<SimilarMovie, SimilarViewHolderIndividual>(SimilarDiffUtilCallBack())
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolderIndividual {
            return SimilarViewHolderIndividual(
                binding = MovieItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onSimilarItemClick = onSimilarItemClick
            )
        }

        override fun onBindViewHolder(holder: SimilarViewHolderIndividual, position: Int) {
            val item = getItem(position)
            holder.bind(item)
        }

    }

    class SimilarViewHolderIndividual(
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
                    Locale.getDefault() == Locale("ru", "RU") -> item.nameRu?:item.nameEn?:item.nameOriginal?:""
                    else -> item.nameEn?:item.nameOriginal?:""
                }
                movieRating.isVisible = false

                binding.root.setOnClickListener {
                    onSimilarItemClick(item)
                }
            }
        }
    }

