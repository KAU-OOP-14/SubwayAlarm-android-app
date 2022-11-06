package com.example.subway_alarm.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.ActivityBookmarkBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.ui.adapter.StationsAdapter


class BookmarkActivity : AppCompatActivity() {

    // 임시로 favorite list 할당
    var stations: MutableList<Station> = mutableListOf(
        Station("화전",1016, mutableListOf(10)),
        Station("홍대입구",1016, mutableListOf(10)),
        Station("강매",1016, mutableListOf(10)),
        Station("수색",1016, mutableListOf(10)),
    )

    lateinit var binding : ActivityBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recStations.layoutManager = LinearLayoutManager(this)
        binding.recStations.adapter = StationsAdapter(stations)

        binding.btnBack2main.setOnClickListener {
            finish()
        }

        // 즐겨찾기에서 역을 클릭하면 MainFragment로 이동

    }

}

