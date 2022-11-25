package com.example.subway_alarm.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentBookmarkBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.ui.adapter.StationsAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookmarkFragment : Fragment() {
    var binding: FragmentBookmarkBinding? = null
    var stations: MutableList<Station> = mutableListOf(
        Station("화전",1016, mutableListOf(10)),
        Station("홍대입구",1016, mutableListOf(10)),
        Station("강매",1016, mutableListOf(10)),
        Station("수색",1016, mutableListOf(10)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        binding?.recStations?.layoutManager = LinearLayoutManager(context)
        binding?.recStations?.adapter = StationsAdapter(stations)

        binding?.btnBack2main?.setOnClickListener{
            findNavController().navigate((R.id.action_bookmarkFragment_to_entryFragment))
        }
        return binding?.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}