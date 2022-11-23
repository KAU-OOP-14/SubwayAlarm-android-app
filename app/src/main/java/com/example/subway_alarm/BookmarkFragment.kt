package com.example.subway_alarm

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.FragmentBookmarkBinding
import com.example.subway_alarm.databinding.FragmentSearchBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.ui.adapter.SearchedListAdapter
import com.example.subway_alarm.ui.adapter.StationsAdapter
import com.example.subway_alarm.ui.fragments.MainFragment
import com.example.subway_alarm.viewModel.ViewModelImpl
import com.example.subway_alarm.viewModel.listener.OnItemClick
import com.example.subway_alarm.viewModel.listener.OnSearchResultClick
import org.koin.android.viewmodel.ext.android.viewModel


class BookmarkFragment : Fragment(), OnItemClick {
    var binding: FragmentBookmarkBinding? = null
    val viewModel by viewModel<ViewModelImpl>()
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

        // 리사이컬뷰를 adapter와 연결, adapter는 한번만 생성합니다.
        binding?.recStations?.layoutManager = LinearLayoutManager(context)
        binding?.recStations?.adapter = StationsAdapter(stations, this)

        binding?.btnBack2main?.setOnClickListener(){
            findNavController().navigate(R.id.action_bookmarkFragment_to_entryFragment)
        }

        return binding?.root


    }

    override fun onItemClick(stationId: Int) {
        val bundle = bundleOf("open" to true)
        viewModel.onStationSelect(stationId)
        findNavController().navigate(R.id.action_bookmarkFragment_to_entryFragment, bundle)
    }

}
