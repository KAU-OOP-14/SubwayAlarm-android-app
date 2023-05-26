package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.databinding.ListStationArrivalBinding
import com.example.subway_alarm.model.api.dataModel.ApiModel

class StationDataAdapter(private val apiModelList: List<ApiModel>): RecyclerView.Adapter<StationDataAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListStationArrivalBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //api 가 최대 2개만 뜹니다.
        holder.bind(apiModelList[position])
    }

    override fun getItemCount(): Int {
        return apiModelList.size
    }

    inner class Holder(private val binding: ListStationArrivalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(apiModel: ApiModel) {
            binding.txtDestination.text = apiModel.bstatnNm
            binding.txtArrivalTime.text = apiModel.arvlMsg2
        }
    }
}