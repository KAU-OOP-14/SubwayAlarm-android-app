package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.BtnLineNumberBinding
import com.example.subway_alarm.databinding.ListStationArrivalBinding
import com.example.subway_alarm.model.api.dataModel.ApiModel

class StationDataAdapter(val apiModelList: List<ApiModel>): RecyclerView.Adapter<StationDataAdapter.Holder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListStationArrivalBinding.inflate(LayoutInflater.from(parent.context))
        val holder = Holder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(apiModelList[position])
    }

    override fun getItemCount(): Int {
        return apiModelList.size
    }

    inner class Holder(private val binding: ListStationArrivalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(apiModel: ApiModel) {
            binding.txtDestination.text = apiModel.trainLineNm
            binding.txtArrivalTime.text = apiModel.arvlMsg2
            binding.imageButton3.setImageResource(R.drawable.ic_baseline_alarm_24)
        }
    }
}