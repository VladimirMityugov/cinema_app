package com.example.skillcinemaapp.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinemaapp.data.remote.filters_dto.CountryFilters
import com.example.skillcinemaapp.databinding.SearchSelectionItemBinding

private const val TAG = "COUNTRY_ADAPTER"
class SearchCountrySelectionAdapter(
    val onItemClick: (CountryFilters) -> Unit
) : ListAdapter<CountryFilters, CountryViewHolder>(DiffUtilCallBackCountrySelection()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(
            binding = SearchSelectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        Log.d(TAG, "Current list is $currentList")
        val item = getItem(position)
       holder.bind(item)
    }
}

class CountryViewHolder(
    val binding: SearchSelectionItemBinding,
    val onItemClick: (CountryFilters) -> Unit
) : ViewHolder(binding.root) {
    fun bind(item: CountryFilters) {
        with(binding) {

            searchItem.text = item.country?:""

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

class DiffUtilCallBackCountrySelection : DiffUtil.ItemCallback<CountryFilters>() {
    override fun areItemsTheSame(oldItem: CountryFilters, newItem: CountryFilters): Boolean {
        return oldItem.id== newItem.id
    }

    override fun areContentsTheSame(oldItem: CountryFilters, newItem: CountryFilters): Boolean {
        return oldItem == newItem
    }
}




