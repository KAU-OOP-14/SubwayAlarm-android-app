package com.example.subway_alarm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.SearchedListStationsBinding
import com.example.subway_alarm.model.STATION_ID_UNIT
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.viewModel.listener.OnBookmarkClick
import com.example.subway_alarm.viewModel.listener.OnSearchResultClick

class SearchedListAdapter(val stationList: MutableList<Station>, clickListener: OnSearchResultClick, starListener: OnBookmarkClick)
    : RecyclerView.Adapter<SearchedListAdapter.Holder>(){
    val clickCallback = clickListener
    val starCallback = starListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SearchedListStationsBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    // 렌더링 역활
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(stationList[position])
    }

    override fun getItemCount() = stationList.size


    inner class Holder(private val binding: SearchedListStationsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(station: Station){
            binding.btnLine.setImageResource(when(station.id / STATION_ID_UNIT){
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
            // 즐겨찾기 중이라면 색상을 노랗게 합니다.
            if(station.isFavorited) {
                binding.btnFavorite.setImageResource(R.drawable.fill_star)
            }
            else {
                binding.btnFavorite.setImageResource(R.drawable.empty_star)
            }

            // 역을 검색할 시 호출되는 함수로 view Model의 curStation을 변경시
            binding.root.setOnClickListener{
                Toast.makeText(binding.root.context,"${station.id/STATION_ID_UNIT}호선 ${station.stationName}역",
                    Toast.LENGTH_SHORT).show()
                clickCallback.onSearchResultClick(station.id)
            }
            binding.btnFavorite.setOnClickListener {
                if(station.isFavorited) {
                    binding.btnFavorite.setImageResource(R.drawable.empty_star)
                }
                else {
                    binding.btnFavorite.setImageResource(R.drawable.fill_star)
                }
                starCallback.onBookmarkClick(station.id)
            }
        }
    }
}