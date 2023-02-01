package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.images_dto.Image
import com.example.skillcinemaapp.databinding.GalleryItemBinding


class GalleryAdapterCommon(
    val onImageClick: (Image) -> Unit
) :
    ListAdapter<Image, ImageViewHolderCommon>(DiffUtilCallBackImages()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolderCommon {
        return ImageViewHolderCommon(
            binding = GalleryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onImageClick = onImageClick
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolderCommon, position: Int) {
        val image = getItem(position)
        if (image != null) {
            holder.bind(image)
        }
    }
}

class ImageViewHolderCommon(
    val binding: GalleryItemBinding,
    val onImageClick: (Image) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(image:Image) {
        with(binding) {
            Glide
                .with(galleryMoviePicture.context)
                .load(image.imageUrl)
                .centerCrop()
                .into(galleryMoviePicture)
        }
        binding.root.setOnClickListener {
            onImageClick(image)
        }
    }
}



