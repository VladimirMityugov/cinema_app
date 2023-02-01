package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.premiers_dto.Item
import com.example.skillcinemaapp.databinding.MovieItemSelectionBinding



class PremiersAdapterIndividual(
    val onPremiersItemClick: (Item) -> Unit
) : ListAdapter<Item, PremiersViewHolderIndividual>(PremiersDiffUtilCallBack()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PremiersViewHolderIndividual {
        return PremiersViewHolderIndividual(
            binding = MovieItemSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onPremiersItemClick = onPremiersItemClick
        )
    }

    override fun onBindViewHolder(holder: PremiersViewHolderIndividual, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class PremiersViewHolderIndividual(
    val binding: MovieItemSelectionBinding,
    val onPremiersItemClick: (Item) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: Item) {
        with(binding) {
            Glide
                .with(moviePicture.context)
                .load(item.posterUrl)
                .centerCrop()
                .into(moviePicture)

            if(item.watched_status !=null){
                watchedStatus.isVisible = item.watched_status == true
            } else watchedStatus.isVisible = false

            movieGenre.text = item.genres.firstOrNull()?.genre
            movieTitle.text = item.nameRu
            movieRating.isVisible = false
        }
        binding.root.setOnClickListener {
            onPremiersItemClick(item)
        }
    }
}




