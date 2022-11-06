package com.example.subway_alarm.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.ActivityBookmarkBinding
import com.example.subway_alarm.model.Station


class BookmarkActivity : AppCompatActivity() {

    val stations = arrayOf()

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

