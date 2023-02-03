package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding
import java.util.*


open class TopAdapterIndividual(
    val onTopItemClick: (Film) -> Unit
) : PagingDataAdapter<Film, TopViewHolderIndividual>(TopAdapterCommon.DiffUtilCallBackTop()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopViewHolderIndividual {
        return TopViewHolderIndividual(
            binding = MovieItemSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onTopItemClick
        )
    }


    override fun onBindViewHolder(holder: TopViewHolderIndividual, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

class TopViewHolderIndividual(
    private val binding: MovieItemSelectionBinding,
    val onItemClick: (Film) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Film) {
        with(binding) {
            Glide
                .with(moviePicture.context)
                .load(item.posterUrl)
                .centerCrop()
                .into(moviePicture)

            if(item.watchedStatus !=null){
               watchedStatus.isVisible = item.watchedStatus == true
            } else watchedStatus.isVisible = false

            movieGenre.text = item.genres.firstOrNull()?.genre
            movieTitle.text = when {
                Locale.getDefault() == Locale("ru", "RU") -> item.nameRu?:item.nameEn?:""
                else -> item.nameEn?:""
            }
            movieRating.text = item.rating
        }
        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}


