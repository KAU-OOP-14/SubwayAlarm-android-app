package com.example.subway_alarm.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentBookmarkBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.ui.adapter.StationsAdapter
import com.example.subway_alarm.viewModel.ArrivalViewModel
import com.example.subway_alarm.viewModel.SearchViewModel
import com.example.subway_alarm.viewModel.listener.OnItemClick
import org.koin.android.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BookmarkFragment : Fragment(), OnItemClick {
    var binding: FragmentBookmarkBinding? = null
    val viewModel by viewModel<ArrivalViewModel>()
    private var stations: MutableList<Station> = mutableListOf()


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

        for (line in Subway.lines) {
            stations.addAll(line.stations)
        }

        binding?.recStations?.layoutManager = LinearLayoutManager(context)
        binding?.recStations?.adapter = StationsAdapter(stations, this)

        binding?.imageBack?.setOnClickListener{
            findNavController().navigate((R.id.action_bookmarkFragment_to_entryFragment))
        }
        return binding?.root
    }


    override fun onItemClick(stationId: Int) {
        val bundle = bundleOf("stationId" to stationId)
        findNavController().navigate(R.id.action_bookmarkFragment_to_entryFragment, bundle)
    }
}