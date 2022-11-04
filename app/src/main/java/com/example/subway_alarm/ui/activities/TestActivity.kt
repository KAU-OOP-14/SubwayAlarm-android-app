package com.example.subway_alarm.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.subway_alarm.databinding.ActivityTestBinding
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.Station

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish() // 이전 화면으로 돌아간다.
        }

        binding.btnSearch.setOnClickListener {
            val resultText = binding.editText.text.toString()
            var resultStation: MutableList<Station>? = Subway.searchStations(resultText)
            if(resultStation != null){
                binding.txtStationName.text = resultStation[0].stationName
                binding.txtId.text = resultStation[0].id.toString()
                val leftS = resultStation[0].leftStation
                val left2S = resultStation[0].left2Station
                val rightS = resultStation[0].rightStation
                val right2S = resultStation[0].right2Station
                binding.txtLeftStation.text = "${leftS?.stationName ?: "null"}, ${left2S?.stationName ?: "null"}"
                binding.txtRightStation.text = "${rightS?.stationName ?: "null"}, ${right2S?.stationName ?: "null"}"
                binding.txtEndpoint.text = "${resultStation[0].endPoint[0]}, ${resultStation[0].endPoint[1]}"
            }
            else{
                binding.txtStationName.text = "Can Not Find"
            }
        }

    }
}