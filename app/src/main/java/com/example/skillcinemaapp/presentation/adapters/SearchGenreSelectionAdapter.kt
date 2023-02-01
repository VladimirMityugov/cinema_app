package com.example.skillcinemaapp.presentation.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinemaapp.data.remote.filters_dto.GenreFilters
import com.example.skillcinemaapp.databinding.SearchSelectionItemBinding

class SearchGenreSelectionAdapter(
    val onItemClick: (GenreFilters) -> Unit
) : ListAdapter<GenreFilters, GenreViewHolder>(DiffUtilCallBackGenreSelection()) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            binding = SearchSelectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class GenreViewHolder(
    val binding: SearchSelectionItemBinding,
    val onItemClick: (GenreFilters) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: GenreFilters) {
        with(binding) {

            searchItem.text = item.genre?.replaceFirstChar { it -> it.uppercaseChar() }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

class DiffUtilCallBackGenreSelection : DiffUtil.ItemCallback<GenreFilters>() {
    override fun areItemsTheSame(oldItem: GenreFilters, newItem: GenreFilters): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GenreFilters, newItem: GenreFilters): Boolean {
        return oldItem == newItem
    }
}




