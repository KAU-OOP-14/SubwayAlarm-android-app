package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.databinding.ListStationArrivalBinding
import com.example.subway_alarm.model.api.dataModel.ApiModel

class StationDataAdapter(val apiModelList: List<ApiModel>): RecyclerView.Adapter<StationDataAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListStationArrivalBinding.inflate(LayoutInflater.from(parent.context))
        val holder = Holder(binding)
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 최대 3개의 api 데이터를 보여줍니다.
        if (position >= 3) return
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