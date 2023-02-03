package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skillcinemaapp.data.remote.staff_dto.StaffDto
import com.example.skillcinemaapp.databinding.StaffItemBinding
import java.util.Locale


class ActorAdapter(
    val onActorClick: (StaffDto) -> Unit
) :
    ListAdapter<StaffDto, ActorViewHolder>(DiffUtilCallBackStaff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            binding = StaffItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onActorClick = onActorClick
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = getItem(position)
        if (actor != null) {
            holder.bind(actor)
        }
    }
}

class ActorViewHolder(
    val binding: StaffItemBinding,
    val onActorClick: (StaffDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(actor: StaffDto) {
        with(binding) {
            if (actor.professionKey == ACTOR) {
                Glide
                    .with(personPhoto.context)
                    .load(actor.posterUrl)
                    .centerCrop()
                    .into(personPhoto)

                if (Locale.getDefault() == Locale("ru","RU")) {
                    personName.text = actor.nameRu ?: actor.nameEn ?: ""
                } else {
                    personName.text = actor.nameEn ?: ""
                }

                description.text = actor.description ?: ""
            }
        }
        binding.root.setOnClickListener {
            onActorClick(actor)
        }
    }

    companion object {
        private const val ACTOR = "ACTOR"
    }
}


