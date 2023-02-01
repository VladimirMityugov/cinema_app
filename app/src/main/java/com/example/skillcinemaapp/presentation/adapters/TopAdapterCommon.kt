package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.popular_dto.Film
import com.example.skillcinemaapp.databinding.MovieItemBinding
import com.example.skillcinemaapp.databinding.ShowAllItemBinding



open class TopAdapterCommon (
    val onTopItemClick: (Film) -> Unit,
    val onShowAllTopClick: (View) -> Unit
        ) : ListAdapter<Film, RecyclerView.ViewHolder>(DiffUtilCallBackTop()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == ITEM_MOVIES) {
            TopViewHolderCommon(
                binding = MovieItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ),
                onItemClick = onTopItemClick
            )
        } else ShowAllViewHolder(
            binding = ShowAllItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            onShowAllItemClick = onShowAllTopClick
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item.let {
            if (getItemViewType(position) == ITEM_MOVIES) {
                (holder as TopViewHolderCommon).bind(it!!)
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

    class TopViewHolderCommon(
        private val binding: MovieItemBinding,
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
                    watchedStatus.isVisible = item.watchedStatus== true
                } else watchedStatus.isVisible = false

                movieGenre.text = item.genres.firstOrNull()?.genre
                movieTitle.text = item.nameRu
                movieRating.text = item.rating
            }
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }


    class DiffUtilCallBackTop : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
            return oldItem == newItem
        }
    }
}

