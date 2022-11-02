package com.example.subway_alarm.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.subway_alarm.databinding.FragmentMainBinding
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MainFragment : Fragment() {
    var binding: FragmentMainBinding? = null
    val viewModel: ViewModelImpl by viewModel()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

        /* View Model과 View 연결 */
        viewModel.data.observe(viewLifecycleOwner, Observer {
        })

        //뒤로 버튼 클릭시 이벤트
        binding?.btnBack?.setOnClickListener {
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        }

        // 왼쪽 역 클릭시 이벤트
        binding?.btnLeft?.setOnClickListener {
            viewModel.goLeft()
        }

        //오른쪽 역 클릭시 이벤트
        binding?.btnRight?.setOnClickListener {
            viewModel.goRight()
        }

        //알람 버튼 클릭시 이벤트
        binding?.btnAlarm?.setOnClickListener {
            viewModel.setAlarm()
        }

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}