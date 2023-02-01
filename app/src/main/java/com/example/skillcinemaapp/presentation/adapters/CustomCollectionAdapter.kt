package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.data.local.entities.CustomCollection
import com.example.skillcinemaapp.databinding.CustomCollectionInProfileBinding


open class CustomCollectionAdapter(
    val onCollectionItemClick: (CustomCollection) -> Unit,
    val onDeleteCollectionClick: (String) -> Unit
) :
    ListAdapter<CustomCollection, CustomSelectionViewHolder>(DiffUtilCallBackCustomCollection()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomSelectionViewHolder {

        return CustomSelectionViewHolder(
            binding = CustomCollectionInProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCollectionItemClick = onCollectionItemClick,
            onDeleteCollectionClick = onDeleteCollectionClick
        )
    }


    override fun onBindViewHolder(holder: CustomSelectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class CustomSelectionViewHolder(
    val binding: CustomCollectionInProfileBinding,
    val onCollectionItemClick: (CustomCollection) -> Unit,
    val onDeleteCollectionClick: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CustomCollection) {
        with(binding) {
            customCollectionTitle.text = item.collectionName

            if (item.movieId == 0) {
                numberInCustomCollection.isVisible = false
            } else {
                numberInCustomCollection.isVisible = true
                numberInCustomCollection.text = item.movieId.toString()
            }

            binding.root.setOnClickListener {
                onCollectionItemClick(item)
            }

            closeButton.setOnClickListener {
                onDeleteCollectionClick(item.collectionName)
            }
        }
    }
}

class DiffUtilCallBackCustomCollection : DiffUtil.ItemCallback<CustomCollection>() {
    override fun areItemsTheSame(
        oldItem: CustomCollection,
        newItem: CustomCollection
    ): Boolean {
        return oldItem.collectionName == newItem.collectionName
    }

    override fun areContentsTheSame(
        oldItem: CustomCollection,
        newItem: CustomCollection
    ): Boolean {
        return oldItem == newItem
    }
}


