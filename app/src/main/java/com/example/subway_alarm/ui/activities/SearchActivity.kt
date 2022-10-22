package com.example.subway_alarm.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.subway_alarm.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}