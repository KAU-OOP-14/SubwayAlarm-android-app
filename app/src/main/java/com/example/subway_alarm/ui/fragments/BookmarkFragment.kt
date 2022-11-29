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
import com.example.subway_alarm.viewModel.BookmarkViewModel
import com.example.subway_alarm.viewModel.listener.OnBookmarkClick
import com.example.subway_alarm.viewModel.listener.OnBookmarkDelete
import org.koin.android.viewmodel.ext.android.sharedViewModel



class BookmarkFragment : Fragment(), OnBookmarkClick, OnBookmarkDelete {
    var binding: FragmentBookmarkBinding? = null
    var stations: MutableList<Station> = mutableListOf()
    val viewModel by sharedViewModel<BookmarkViewModel>()

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
        binding?.recStations?.adapter = StationsAdapter(stations, this, this)

        binding?.imageBack?.setOnClickListener{
            findNavController().navigate((R.id.action_bookmarkFragment_to_entryFragment))
        }

        viewModel.favorites.observe(viewLifecycleOwner) { stationIdList->
//            // 즐겨찾기 해제
//            for (station in stations) {
//                Subway.searchWithId(station.id).isFavorited = false
//            }

            stations.clear()
            for ( id in stationIdList ) {
                stations.add(
                    //즐겨찾기 등록
                    Subway.searchWithId(id).apply {
//                        isFavorited = true
                    }
                )
            }
            binding?.recStations?.adapter?.notifyDataSetChanged()
        }

        return binding?.root
    }




    override fun onBookmarkClick(stationId: Int) {
        val bundle = bundleOf("stationId" to stationId)
        findNavController().navigate(R.id.action_bookmarkFragment_to_entryFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onBookmarkDelete(stationId: Int) {
        viewModel.onBookmarkClick(stationId)
    }
}