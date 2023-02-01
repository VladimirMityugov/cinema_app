package com.example.skillcinemaapp.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.databinding.YearItemBinding


class PeriodToAdapter(
    val onYearToClick: (Int) -> Unit,
    private val selectedItem: Int,
    private val yearsOK: Boolean
) :
    ListAdapter<Int, YearToViewHolder>(DiffUtilCallBackYears()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearToViewHolder {
        return YearToViewHolder(
            binding = YearItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onYearToClick = onYearToClick,
            selectedItem = selectedItem,
            yearsOK = yearsOK
        )
    }

    override fun onBindViewHolder(holder: YearToViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }

    }
}

class YearToViewHolder(
    val binding: YearItemBinding,
    val onYearToClick: (Int) -> Unit,
    val selectedItem: Int,
    private val yearsOK: Boolean
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Int) {
        with(binding) {
            year.text = item.toString()
            if(item == selectedItem){
                year.isEnabled = true
                if(!yearsOK)year.setTextColor(Color.BLACK)
            }else {
                year.isEnabled = false
            }
        }
        binding.root.setOnClickListener {
            onYearToClick(item)
        }
    }
}



