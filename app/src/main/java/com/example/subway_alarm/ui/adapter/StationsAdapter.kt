package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ListStatoinsBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.viewModel.listener.OnItemClick

class StationsAdapter(val stations: MutableList<Station>, listener : OnItemClick)
    : RecyclerView.Adapter<StationsAdapter.Holder>() {
    val callback = listener

//    class StationViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
//        val textView : TextView = itemView.findViewById(R.id.station_name)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListStatoinsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stations[position])

//        holder.itemView.setOnClickListener {
//            onItemClick?.invoke(stations)
//        }
    }

    override fun getItemCount() = stations.size


    inner class Holder(private val binding: ListStatoinsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) {
            binding.stationName.text = station.stationName
            binding.root.setOnClickListener {
                callback.onItemClick(station.id)
            }
        }

    }
}