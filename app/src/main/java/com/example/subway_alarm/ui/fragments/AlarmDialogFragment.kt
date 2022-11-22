package com.example.subway_alarm.ui.fragments

import android.app.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.subway_alarm.databinding.FragmentAlarmDialogBinding
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel


class AlarmDialogFragment : DialogFragment(){
    var binding: FragmentAlarmDialogBinding?  = null
    private val viewModel by viewModel<ViewModelImpl>()
    private var minute: Int = 0
    private var second: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmDialogBinding.inflate(inflater, container, false)
        binding?.numPickerMin?.minValue = 0
        binding?.numPickerMin?.maxValue = 59
        binding?.numPickerSec?.minValue = 0
        binding?.numPickerSec?.maxValue = 59


        binding?.numPickerMin?.setOnValueChangedListener { _, _, newVal ->
            minute = newVal
        }
        binding?.numPickerSec?.setOnValueChangedListener { _, _, newVal ->
            second = newVal
        }

        binding?.txtApply?.setOnClickListener {
            //알람이 이미 설정되어 있다면 재설정을 할 것인지 물어봅니다.
            if(viewModel.alarmTime.value > 0) {
                val builder = AlertDialog.Builder(this.activity)
                builder
                    .setTitle("새로운 알람 설정")
                    .setPositiveButton("OK") { _, _ ->
                        viewModel.resetAlarm(minute * 60 + second)
                        Toast.makeText(context, "새로운 알람이 설정되었습니다", Toast.LENGTH_SHORT).show()
                        (activity as MainActivity).onAlarmSet()
                    }
                    .setNegativeButton("Cancel") { _, _ ->

                    }
                    .show()
            }
            else {
                Toast.makeText(context, "알람이 설정되었습니다", Toast.LENGTH_SHORT).show()
                viewModel.setAlarm(minute * 60 + second)
                (activity as MainActivity).onAlarmSet()
                this.dismiss()
            }
        }


        return binding?.root
    }

    companion object {
        const val Tag = "test"
        @JvmStatic
        fun newInstance() =
            AlarmDialogFragment().apply {
            }
    }

}