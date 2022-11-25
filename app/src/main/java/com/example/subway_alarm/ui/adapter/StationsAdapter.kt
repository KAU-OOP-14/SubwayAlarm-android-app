package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.databinding.ListStatoinsBinding
import com.example.subway_alarm.model.Station

class StationsAdapter(val stations: MutableList<Station>)
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
        fun bind(station: Station) {
            binding.stationName.text = station.stationName
            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context,"bookmark is selected",
                    Toast.LENGTH_SHORT).show()
            }
        }

    }
}