package com.example.subway_alarm.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    var binding: FragmentSettingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding?.btnTop?.setOnClickListener{
            findNavController().navigate(R.id.action_entryFragment_to_settingFragment)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnTop?.setOnClickListener{
            findNavController().navigate(R.id.action_settingFragment_to_entryFragment)
        }
    }

    // 메모리 관리를 위해서
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}