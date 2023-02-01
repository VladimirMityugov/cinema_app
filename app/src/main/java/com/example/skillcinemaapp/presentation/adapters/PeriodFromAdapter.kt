package com.example.skillcinemaapp.presentation.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.databinding.YearItemBinding



class PeriodFromAdapter(
    val onYearFromClick: (Int) -> Unit,
    private val selectedItem: Int,
    private val yearsOK:Boolean
) :
    ListAdapter<Int, YearViewHolder>(DiffUtilCallBackYears()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        return YearViewHolder(
            binding = YearItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onYearFromClick = onYearFromClick,
            selectedItem = selectedItem,
            yearsOK = yearsOK
        )
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}

class YearViewHolder(
    val binding: YearItemBinding,
    val onYearFromClick: (Int) -> Unit,
    val selectedItem: Int,
    private val yearsOK:Boolean
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
            onYearFromClick(item)
        }
    }
}

class DiffUtilCallBackYears : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldImage: Int, newImage: Int): Boolean {
        return oldImage == newImage
    }

    override fun areContentsTheSame(oldImage: Int, newImage: Int): Boolean {
        return oldImage == newImage
    }

}


