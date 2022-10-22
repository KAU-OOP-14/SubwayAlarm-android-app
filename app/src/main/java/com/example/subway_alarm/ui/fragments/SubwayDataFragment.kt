package com.example.subway_alarm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.subway_alarm.databinding.FragmentSubwayDataBinding

private const val PARAM1 = "test1"
private const val ARG_PARAM2 = "param2"


class SubwayDataFragment : Fragment() {
    var binding: FragmentSubwayDataBinding? = null
    // TODO: Rename and change types of parameters
    private var arriveTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arriveTime = it.getString(PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSubwayDataBinding.inflate(inflater, container, false)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        binding?.btnClose?.setOnClickListener {
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        }

        binding?.txtArriveTime?.text = "도착 예정 시간 : " + arriveTime.toString()

        return binding?.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            SubwayDataFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM1, param1)
                }
            }
    }


}