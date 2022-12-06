package com.example.subway_alarm.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentMainBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.model.STATION_ID_UNIT
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.ui.adapter.LineNumAdapter
import com.example.subway_alarm.ui.adapter.StationDataAdapter
import com.example.subway_alarm.viewModel.ArrivalViewModel
import com.example.subway_alarm.viewModel.ArrivalViewModel.Direction
import com.example.subway_alarm.viewModel.BookmarkViewModel
import com.example.subway_alarm.viewModel.listener.OnAlarmSet
import com.example.subway_alarm.viewModel.listener.OnLineChange
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel


class MainFragment : BottomSheetDialogFragment(), OnLineChange, OnAlarmSet {

    private var paramId: Int = 2005 // 홍대입구
    var binding: FragmentMainBinding? = null
    val viewModel by sharedViewModel<ArrivalViewModel>()
    val bookmarkViewModel by sharedViewModel<BookmarkViewModel>()
    var lineNumbers: Array<Int> = arrayOf()
    var apiModelList: List<ApiModel> = listOf()
    lateinit var entryFragment: EntryFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramId = it.getInt("stationId")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        entryFragment = EntryFragment()
        entryFragment.binding?.frgMain?.visibility = View.VISIBLE

        viewModel.onStationSelect(paramId)

        /* View Model과 View 연결 */
        viewModel.leftApi.observe(viewLifecycleOwner, Observer {
            var data = ""
            apiModelList = it
            binding?.recLeft?.adapter = StationDataAdapter(apiModelList)
            for (model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
        })
        viewModel.rightApi.observe(viewLifecycleOwner, Observer {
            var data = ""
            apiModelList = it
            binding?.recRight?.adapter = StationDataAdapter(apiModelList)
            for (model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
        })

        // view model의 cur station 관찰
        viewModel.curStation.observe(viewLifecycleOwner, Observer {
            binding?.txtStationName?.text = it.stationName
            binding?.txtStationName?.isSelected = true
            binding?.txtLeftStation?.text = it.leftStation?.stationName ?: "역 정보 없음"
            binding?.txtLeftStation?.isSelected = true
            binding?.txtRightStation?.text = it.rightStation?.stationName ?: "역 정보 없음"
            binding?.txtRightStation?.isSelected = true
            binding?.stationBackground?.setBackgroundColor(getColor(it))
            binding?.txtStationName?.setTextColor(getColor(it))
            binding?.txtLeftDirection?.text = it.endPoint[0]
            binding?.txtRightDirection?.text = it.endPoint[1]
            lineNumbers = it.lineList.toTypedArray()
            binding?.recLine?.adapter = LineNumAdapter(lineNumbers, this)

            Subway.getStation(viewModel.curStation.value.id)?.let {
                if (it.isFavorited) {
                    binding?.btnStar?.setImageResource(R.drawable.fill_star)
                } else {
                    binding?.btnStar?.setImageResource(R.drawable.empty_star)
                }
            }
        })

        // adapter과 연결
        binding?.recLine?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.recLine?.adapter = LineNumAdapter(lineNumbers, this)
        binding?.recLeft?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recLeft?.adapter = StationDataAdapter(apiModelList)
        binding?.recRight?.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recRight?.adapter = StationDataAdapter(apiModelList)

        // 즐겨찾기가 되어 있다면 강조됩니다.
        Subway.getStation(viewModel.curStation.value.id)?.let {
            if (it.isFavorited)
                binding?.btnStar?.setImageResource(R.drawable.fill_star)
        }


        // 왼쪽 역 버튼 클릭시 이벤트
        binding?.btnLeft?.setOnClickListener {
            val array: Array<String>? = viewModel.onCrossedLine("left")
            if (array == null)
                viewModel.gotoStation(Direction.LEFT)
            else {
                var index: Int
                val builder = AlertDialog.Builder(this.activity)
                builder
                    .setTitle("Selct Station")
                    .setItems(array) { _, which ->
                        Toast.makeText(
                            this.activity,
                            "${array[which]} is Selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        index = which
                        viewModel.gotoStation(Direction.LEFT, index)
                    }.show()

            }
        }

        //오른쪽 역 버튼 클릭시 이벤트
        binding?.btnRight?.setOnClickListener {
            val array: Array<String>? = viewModel.onCrossedLine("right")
            if (array == null)
                viewModel.gotoStation(Direction.RIGHT)
            else {
                var index: Int
                val builder = AlertDialog.Builder(this.activity)
                builder
                    .setTitle("Selct Station")
                    .setItems(array) { _, which ->
                        Toast.makeText(
                            this.activity,
                            "${array[which]} is Selected",
                            Toast.LENGTH_SHORT
                        ).show()
                        index = which
                        println("index : $index")
                        viewModel.gotoStation(Direction.RIGHT, index)
                    }.show()

            }
        }

        binding?.btnAlarm?.setOnClickListener {
            activity?.let {
                AlarmDialogFragment.newInstance().show(it.supportFragmentManager, "")
            }
        }

        binding?.btnStar?.setOnClickListener {
            // 즐겨찾기 취소
            Subway.getStation(viewModel.curStation.value.id)?.let {
                if (it.isFavorited) {
                    binding?.btnStar?.setImageResource(R.drawable.empty_star)
                } else {
                    binding?.btnStar?.setImageResource(R.drawable.fill_star)
                }
            }

            bookmarkViewModel.onBookmarkClick(viewModel.curStation.value.id)
        }


        return binding?.root
    }

    // 메모리 낭비를 줄이기
    override fun onDestroy() {
        entryFragment.binding?.frgMain?.visibility = View.INVISIBLE
        super.onDestroy()
        binding = null
    }

    override fun changeLine(lineNum: Int) {
        viewModel.changeLine(lineNum)
    }

    fun getColor(station: Station): Int {
        when (station.id / STATION_ID_UNIT) {
            1 -> return Color.parseColor("#FF0D3692")
            2 -> return Color.parseColor("#FF33A23D")
            3 -> return Color.parseColor("#FFFE5D10")
            4 -> return Color.parseColor("#FF00A2D1")
            5 -> return Color.parseColor("#FF8B50A4")
            6 -> return Color.parseColor("#FFC55C1D")
            7 -> return Color.parseColor("#FF54640D")
            8 -> return Color.parseColor("#FFF14C82")
            9 -> return Color.parseColor("#FFAA9872")
            10 -> return Color.parseColor("#FF73C7A6")
            11 -> return Color.parseColor("#FF3681B7")
            12 -> return Color.parseColor("#FF32C6A6")
            13 -> return Color.parseColor("#FFD49B3B")
            14 -> return Color.parseColor("#FFC82127")
            15 -> return Color.parseColor("#FFFFCD12")
            16 -> return Color.parseColor("#FFB0CE18")
            else -> return 0
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(stationId: Int) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putInt("stationId", stationId)
                }
            }
    }

    /** 알람을 설정했을 때 호출하는 callback 입니다.*/
    override fun onAlarmSet() {
        Toast.makeText(this.context, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }
}
