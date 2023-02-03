package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.person_dto.PersonFilm
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding
import java.util.Locale


class BestMoviesAdapter(
    val onBestMovieItemClick: (PersonFilm) -> Unit
) : ListAdapter<PersonFilm, BestMovieViewHolder>(PersonMovieDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestMovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BestMovieViewHolder(
            binding = MovieItemSelectionBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            onBestMovieItemClick = onBestMovieItemClick
        )
    }


    override fun onBindViewHolder(holder: BestMovieViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class BestMovieViewHolder(
    val binding: MovieItemSelectionBinding,
    val onBestMovieItemClick: (PersonFilm) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: PersonFilm) {
        with(binding) {

            moviePicture.isVisible = true
            movieGenre.isVisible = true
            movieTitle.isVisible = true
            movieRating.isVisible = true


            if (item.posterUri != null) {
                Glide
                    .with(moviePicture.context)
                    .load(item.posterUri)
                    .centerCrop()
                    .into(moviePicture)
            }


            val professionKey = item.professionKey
            movieGenre.text = when (professionKey) {
                "ACTOR" -> ACTOR
                "PRODUCER" -> PRODUCER
                "WRITER" -> WRITER
                "DIRECTOR" -> DIRECTOR
                "HIMSELF" -> HIMSELF
                "HERSELF" -> HERSELF
                "HRONO_TITR_MALE" -> HRONO_TITR_MALE
                "HRONO_TITR_FEMALE" -> HRONO_TITR_FEMALE
                "OPERATOR" -> OPERATOR
                "EDITOR" -> EDITOR
                "COMPOSER" -> COMPOSER
                "TRANSLATOR" -> TRANSLATOR
                "DESIGN" -> DESIGN
                "VOICE_DIRECTOR" -> VOICE_DIRECTOR
                else -> UNKNOWN
            }
            if (Locale.getDefault() == Locale("ru","RU")) {
                movieTitle.text = item.nameRu ?: item.nameEn ?: ""
            } else {
                movieTitle.text = item.nameEn ?: ""
            }

            movieRating.text = item.rating ?: ""

            binding.root.setOnClickListener {
                onBestMovieItemClick(item)
            }
        }
    }

    companion object {
        private const val ACTOR = "Актер"
        private const val PRODUCER = "Продюсер"
        private const val WRITER = "Сценарист"
        private const val DIRECTOR = "Режиссер"
        private const val HIMSELF = "Актер: Играет самого себя"
        private const val HERSELF = "Актриса: Играет саму себя"
        private const val HRONO_TITR_MALE = "Актер дубляжа"
        private const val HRONO_TITR_FEMALE = "Актриса дубляжа"
        private const val OPERATOR = "Оператор"
        private const val EDITOR = "Редактор"
        private const val COMPOSER = "Композитор"
        private const val TRANSLATOR = "Переводчик"
        private const val DESIGN = "Дизайнер"
        private const val VOICE_DIRECTOR = "Режиссер озвучания"
        private const val UNKNOWN = "Неизвестно"
    }
}


