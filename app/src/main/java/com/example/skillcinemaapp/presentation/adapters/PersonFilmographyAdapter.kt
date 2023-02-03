package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.databinding.FilmographyMovieItemBinding
import java.util.*


class PersonFilmographyAdapter(
    val onFilmographyMovieItemClick: (PersonFilm) -> Unit
) : ListAdapter<PersonFilm, PersonFilmographyViewHolder>(PersonFilmographyDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonFilmographyViewHolder {
        return PersonFilmographyViewHolder(
            binding = FilmographyMovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onFilmographyMovieItemClick = onFilmographyMovieItemClick
        )
    }

    override fun onBindViewHolder(holder: PersonFilmographyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class PersonFilmographyViewHolder(
    val binding: FilmographyMovieItemBinding,
    val onFilmographyMovieItemClick: (PersonFilm) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: PersonFilm) {
        with(binding) {

            moviePicture.isVisible = true
            movieInfo.isVisible = true
            movieTitle.isVisible = true
            if (item.rating != null) {
                movieRating.isVisible = true
                movieRating.text = item.rating
            } else movieRating.isVisible = false

            movieInfo.text = item.description ?: ""
            if (item.posterUri != null) {
                Glide
                    .with(moviePicture.context)
                    .load(item.posterUri)
                    .centerCrop()
                    .into(moviePicture)
            }

            movieTitle.text = when {
                Locale.getDefault() == Locale.US || Locale.getDefault() == Locale.UK -> item.nameEn ?: ""
                else -> item.nameRu ?: ""
            }

            binding.root.setOnClickListener {
                onFilmographyMovieItemClick(item)
            }
        }
    }
}

class PersonFilmographyDiffUtilCallBack : DiffUtil.ItemCallback<PersonFilm>() {
    override fun areItemsTheSame(oldItem: PersonFilm, newItem: PersonFilm): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(oldItem: PersonFilm, newItem: PersonFilm): Boolean {
        return oldItem == newItem
    }
}


