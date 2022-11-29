package com.example.subway_alarm.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.subway_alarm.databinding.ActivityLoadingBinding
import com.example.subway_alarm.model.db.SubwayBuilder
import com.example.subway_alarm.viewModel.ArrivalViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class LoadingActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoadingBinding
    val viewModel by viewModel<ArrivalViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("loading activity 생성")
        viewModel.onLoading()
        if(viewModel.isLoaded) {
            startMainActivity()
        }
        binding = ActivityLoadingBinding.inflate(layoutInflater)

        // layout의 크기를 px에서 dp로 설정하기 위해 변환하는 과정입니다.
        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics)
                .toInt()
        // 레이아웃을 만들어 progress에 부착합니다. 이걸 안 해주면 제대로 constraint가 붙지 않습니다.
        val layout = ConstraintLayout.LayoutParams(0, height)
        layout.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        layout.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layout.matchConstraintPercentWidth = 0f

        binding.progress.layoutParams = layout

        // SubwayBuilder의 progress live data를 관찰해서 progress bar를 채웁니다.
        SubwayBuilder.progress.observe(this, Observer {
            layout.matchConstraintPercentWidth = it / 100f
            binding.progress.layoutParams = layout
        })


        lifecycleScope.launch {
            while (SubwayBuilder.loading) delay(100)
            viewModel.isLoaded = true
            startMainActivity()
            finish()
        }
        setContentView(binding.root)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        println("loading 파괴")
    }
}
