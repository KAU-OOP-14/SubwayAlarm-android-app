package com.example.subway_alarm.ui.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.ListStationArrivalBinding
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.viewModel.OnAlarmSet

class StationDataAdapter(val apiModelList: List<ApiModel>, listener: OnAlarmSet): RecyclerView.Adapter<StationDataAdapter.Holder>(){
    val callback = listener

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
            binding.txtDestination.text = apiModel.bstatnNm
            binding.txtArrivalTime.text = apiModel.arvlMsg2
            binding.btnAlarm.setImageResource(R.drawable.ic_baseline_alarm_24)
            binding.btnAlarm.setOnClickListener {
                callback.onAlarmSet()
            }
        }
    }
}