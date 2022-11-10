package com.example.subway_alarm.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.subway_alarm.databinding.ActivityLoadingBinding
import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.viewModel.ViewModelImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.jar.Manifest

class LoadingActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoadingBinding
    val viewModel: ViewModelImpl by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.create()
        println("loading activity 생성")
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLoading.text = "Loading"

        lifecycleScope.launch {
            while(SubwayBuilder.loading) delay(100)
            startMainActivity()
        }


    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }
}
