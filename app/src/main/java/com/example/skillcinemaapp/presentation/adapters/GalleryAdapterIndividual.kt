package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.databinding.ImageItemBinding


class GalleryAdapterIndividual(
    val onImageClick: (Image) -> Unit
) :
    PagingDataAdapter<Image, ImageViewHolder>(DiffUtilCallBackImages()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            binding = ImageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onImageClick = onImageClick
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image)
        }
    }
}

class ImageViewHolder(
    val binding: ImageItemBinding,
    val onImageClick: (Image) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image:Image) {
        with(binding) {
            Glide
                .with(imageHolder)
                .load(image.imageUrl)
                .into(imageHolder)

        }
        binding.root.setOnClickListener {
            onImageClick(image)
        }
    }
}

class DiffUtilCallBackImages : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldImage: Image, newImage: Image): Boolean {
        return oldImage.imageUrl == newImage.imageUrl
    }

    override fun areContentsTheSame(oldImage: Image, newImage: Image): Boolean {
        return oldImage == newImage
    }

}


