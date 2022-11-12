package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.SearchedListStationsBinding
import com.example.subway_alarm.model.Station

class SearchedListAdapter(val stationList: MutableList<Station>)
    : RecyclerView.Adapter<SearchedListAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SearchedListStationsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    // 렌더링 역활
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stationList[position])
    }

    override fun getItemCount() = stationList.size


    class Holder(private val binding: SearchedListStationsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(station: Station){
            binding.btnLine.setImageResource(when(station.id / 100){
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
            binding.txtSearchedStationName.text = station.stationName

            // 역을 검색할 시 호출되는 함수로 view Model의 curStation을 변경시키고
            // intent를 활용해서 main Activity로 이동?
            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context,"호선: ${station.id/100} 이름 : ${station.stationName}",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }
}