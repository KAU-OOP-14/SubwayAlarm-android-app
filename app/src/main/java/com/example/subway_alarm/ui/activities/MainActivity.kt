package com.example.subway_alarm.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.subway_alarm.data.repository.StationRepository
import com.example.subway_alarm.data.repository.StationRepositoryImpl
import com.example.subway_alarm.databinding.ActivityMainBinding
import com.example.subway_alarm.ui.fragments.SubwayDataFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    val stationRepository: StationRepository by inject()
    lateinit var binding: ActivityMainBinding
    var testInt: Int = 0

    private fun replaceSubwayDataFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(binding.frgSubwayData.id, fragment)
            commit()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hwajeon.setOnClickListener {
            replaceSubwayDataFragment(SubwayDataFragment.newInstance(getModelData()))
        }
    }

    private fun getModelData(): String {
        return stationRepository.getStationArrivals()
        //return (testInt++).toString() + "0분 후 도착"
    }
}