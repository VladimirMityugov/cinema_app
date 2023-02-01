package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import com.example.skillcinemaapp.databinding.StaffItemBinding


class StaffAdapter(
    val onStaffClick: (StaffDto) -> Unit
) :
    ListAdapter<StaffDto, StaffViewHolder>(DiffUtilCallBackStaff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        return StaffViewHolder(
            binding = StaffItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onStaffClick = onStaffClick
        )
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val person = getItem(position)
        if (person != null) {
            holder.bind(person)
        }
    }
}

class StaffViewHolder(
    val binding: StaffItemBinding,
    val onStaffClick: (StaffDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(person: StaffDto) {
        with(binding) {
            if (person.professionKey != ACTOR) {
                Glide
                    .with(personPhoto.context)
                    .load(person.posterUrl)
                    .centerCrop()
                    .into(personPhoto)

                personName.text = person.nameRu ?: person.nameEn ?: ""
                description.text = person.professionText.dropLast(1)
            }
        }
        binding.root.setOnClickListener {
            onStaffClick(person)
        }
    }

    companion object {
        private const val ACTOR = "ACTOR"
    }
}

class DiffUtilCallBackStaff : DiffUtil.ItemCallback<StaffDto>() {
    override fun areItemsTheSame(oldItem: StaffDto, newItem: StaffDto): Boolean {
        return oldItem.staffId == newItem.staffId
    }

    override fun areContentsTheSame(oldItem: StaffDto, newItem: StaffDto): Boolean {
        return oldItem == newItem
    }

}


