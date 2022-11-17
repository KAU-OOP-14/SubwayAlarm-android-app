package com.example.subway_alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.subway_alarm.databinding.FragmentSearchBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.ui.adapter.SearchedListAdapter
import com.example.subway_alarm.viewModel.ViewModelImpl
import com.example.subway_alarm.viewModel.listener.OnSearchResultClick
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), OnSearchResultClick {
    var binding: FragmentSearchBinding? = null
    val viewModel by viewModel<ViewModelImpl>()
    private var stationList: MutableList<Station> = mutableListOf()


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
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        for (line in Subway.lines) {
            stationList.addAll(line.stations)
        }


        // 리사이컬뷰를 adapter와 연결, adapter는 한번만 생성합니다.
        binding?.recStations?.layoutManager = LinearLayoutManager(context)
        binding?.recStations?.adapter = SearchedListAdapter(stationList, this)

        val searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                // 검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String?): Boolean {
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String?): Boolean {
                    s?.apply {
                        //새로운 어뎁터를 만들지 않고, 데이터만 변경한 후 변경사항을 어뎁터에게 notify
                        stationList.clear()
                        for(station in viewModel.onSearchTextChanged(s)){
                            stationList.add(station)
                        }
                        binding?.recStations?.adapter?.notifyDataSetChanged()
                    }
                    return false
                }

            }
        // SearchView에 OnQueryTextLitener 부착
        binding?.searchStation?.setOnQueryTextListener(searchViewTextListener)



        return binding?.root
    }

    override fun onSearchResultClick(stationId: Int) {
        val bundle = bundleOf("open" to true)
        viewModel.onStationSelect(stationId)
        findNavController().navigate(R.id.action_searchFragment_to_entryFragment, bundle)
    }


    /*
    companion object {
        @JvmStatic
        fun newInstance(stationId: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, stationId)
                }
            }
    }
     */
}