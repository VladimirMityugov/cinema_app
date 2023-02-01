package com.example.skillcinemaapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skillcinemaapp.data.remote.series_info_dto.Episode
import com.example.skillcinemaapp.databinding.SeriesItemBinding
import com.example.skillcinemaapp.databinding.SeriesTotalItemBinding
import java.text.SimpleDateFormat
import java.util.*


private const val DATE_FORMAT = "yyyy-MM-dd"

open class SeriesInfoAdapter(
    val seasonsNumber: Int,
    val episodeNumber: Int
) : ListAdapter<Episode, RecyclerView.ViewHolder>(DiffUtilCallBackSeriesInfo()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == SERIES_INFO) {
            SeriesInfoViewHolder(
                binding = SeriesItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
        } else SeriesTotalViewHolder(
            binding = SeriesTotalItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            seasonsNumber = seasonsNumber,
            episodeNumber = episodeNumber
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item.let {
            if (getItemViewType(position) == SERIES_INFO) {
                (holder as SeriesInfoViewHolder).bind(it!!)
            } else (holder as SeriesTotalViewHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) SERIES_TOTAL
        else SERIES_INFO
    }

    companion object {
        private const val SERIES_INFO = 0
        private const val SERIES_TOTAL = 1
    }

    class SeriesInfoViewHolder(
        private val binding: SeriesItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Episode) {
            with(binding) {
                seriesInfo.text = buildString {
                    append(item.episodeNumber)
                    append(" серия. ")
                    append(item.nameRu?:item.nameEn?:"")
                }
                val calendar = Calendar.getInstance()
                val apiResponseDate = item.releaseDate
                val simpleDateFormat = SimpleDateFormat(
                    DATE_FORMAT,
                    Locale.getDefault()
                )
                val releaseDate = apiResponseDate?.let { simpleDateFormat.parse(it) }
                if(releaseDate !=null){
                    calendar.time = releaseDate
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val year = calendar.get(Calendar.YEAR)
                    val month = getMonthByNumber(calendar.get(Calendar.MONTH))
                    date.text = buildString {
                        append(day)
                        append(" ")
                        append(month)
                        append(" ")
                        append(year)
                    }
                }else date.text = ""
            }
        }

        private fun getMonthByNumber(monthNumber: Int): String {
            val c = Calendar.getInstance()
            val monthDate = SimpleDateFormat("MMMM", Locale.getDefault())
            c[Calendar.MONTH] = monthNumber - 1
            return monthDate.format(c.time)
        }
    }

    class SeriesTotalViewHolder(
        private val binding: SeriesTotalItemBinding,
        val seasonsNumber: Int,
        val episodeNumber: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                seriesTotalInfo.text = buildString {
                    val seasonsNumber = seasonsNumber
                    val episodeNumber = episodeNumber
                    append(
                        seasonsNumber
                    )
                    append(
                        when (seasonsNumber) {
                            0 -> ""
                            1 -> " сезон, "
                            in 2..4 -> " сезона, "
                            else -> " сезонов, "
                        }
                    )
                    append(
                        episodeNumber
                    )
                    append(
                        if(episodeNumber>0){
                        when (episodeNumber.toString().takeLast(1)) {
                            "0" -> " серий."
                            "1" -> " серия."
                            "2","3","4" -> " серии."
                            else -> " серий."
                        }
                    }else ""

                    )
                }
            }
        }
    }
}


class DiffUtilCallBackSeriesInfo : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem.nameRu == newItem.nameRu
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem == newItem
    }
}


