package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.skillcinemaapp.databinding.AdapterFooterBinding

class StateAdapter : LoadStateAdapter<StateAdapter.StateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateViewHolder {
        return StateViewHolder(
            binding = AdapterFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class StateViewHolder(private val binding: AdapterFooterBinding) :
        ViewHolder(binding.root) {

        init {
            val animation = RotateAnimation(
                0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.INFINITE
            animation.duration = 1500

            binding.loader.startAnimation(animation)
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                loader.isVisible = loadState is LoadState.Loading
                footerMessage.isVisible = loadState !is LoadState.Loading
            }
        }
    }


}