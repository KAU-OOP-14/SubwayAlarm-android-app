package com.example.subway_alarm.ui.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.databinding.ListStatoinsBinding
import com.example.subway_alarm.model.Station

class StationsAdapter(val stations: Array<FavoritStation>)
    : RecyclerView.Adapter<StationsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListStatoinsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stations[position])
    }

    override fun getItemCount() = stations.size


    class Holder(private val binding: ListStatoinsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: FavoritStation) {
            binding.stationName.text = station.name
        }

    }
}