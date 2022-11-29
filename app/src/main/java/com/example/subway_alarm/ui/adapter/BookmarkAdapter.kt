package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ListFavoritesBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.viewModel.listener.OnBookmarkClick
import com.example.subway_alarm.viewModel.listener.OnBookmarkDelete

class BookmarkAdapter(val stations: MutableList<Station>, clickListener: OnBookmarkClick, deleteListener: OnBookmarkDelete )
    : RecyclerView.Adapter<BookmarkAdapter.Holder>() {
    val clickCallback = clickListener
    val deleteCallback = deleteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListFavoritesBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stations[position])
    }

    override fun getItemCount() = stations.size


    inner class Holder(private val binding: ListFavoritesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) {
            binding.stationName.text = station.stationName
            binding.btnLine2.setImageResource(when(station.id / 100){
                1 -> R.drawable.line1
                2 -> R.drawable.line2
                3 -> R.drawable.line3
                4 -> R.drawable.line4
                5 -> R.drawable.line5
                6 -> R.drawable.line6
                7 -> R.drawable.line7
                8 -> R.drawable.line8
                9 -> R.drawable.line9
                10 -> R.drawable.line10
                11 -> R.drawable.line11
                12 -> R.drawable.line12
                13 -> R.drawable.line13
                14 -> R.drawable.line14
                15 -> R.drawable.line15
                16 -> R.drawable.line16
                else -> R.drawable.circle   // 없는 경우 그냥 원 그리기
            })
            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context,"bookmark is selected",
                    Toast.LENGTH_SHORT).show()
                clickCallback.onBookmarkClick(station.id)
            }
            binding.btnDelete.setOnClickListener {
                deleteCallback.onBookmarkDelete(station.id)
            }
        }

    }
}