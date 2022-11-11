package com.example.subway_alarm.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.FragmentMainBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.ui.adapter.LineNumAdapter
import com.example.subway_alarm.ui.adapter.StationDataAdapter
import com.example.subway_alarm.viewModel.listener.OnAlarmSet
import com.example.subway_alarm.viewModel.listener.OnLineChange
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel
import com.example.subway_alarm.viewModel.ViewModelImpl.Direction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MainFragment : BottomSheetDialogFragment(), OnLineChange, MainActivity.onBackPressedListener,
    OnAlarmSet {
    var binding: FragmentMainBinding? = null
    val viewModel: ViewModelImpl by viewModel()
    var lineNumbers: Array<Int> = arrayOf()
    var apiModelList: List<ApiModel> = listOf()
    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

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
        binding = FragmentMainBinding.inflate(inflater, container, false)
        mainActivity.binding.frgMain.visibility = View.VISIBLE

        /* View Model과 View 연결 */
        viewModel.leftApi.observe(viewLifecycleOwner, Observer {
            var data = ""
            apiModelList = it
            binding?.recLeft?.adapter = StationDataAdapter(apiModelList,this)
            for( model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
        })
        viewModel.rightApi.observe(viewLifecycleOwner, Observer {
            var data = ""
            apiModelList = it
            binding?.recRight?.adapter = StationDataAdapter(apiModelList,this)
            for( model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
        })

        // view model의 cur station 관찰
        viewModel.curStation.observe(viewLifecycleOwner, Observer {
            binding?.txtStationName?.text = it.stationName
            binding?.txtStationName?.isSelected = true
            binding?.txtLeftStation?.text = it.leftStation?.stationName?:"역 정보 없음"
            binding?.txtLeftStation?.isSelected = true
            binding?.txtRightStation?.text = it.rightStation?.stationName?:"역 정보 없음"
            binding?.txtRightStation?.isSelected = true
            binding?.stationBackground?.setBackgroundColor(getColor(it))
            binding?.txtStationName?.setTextColor(getColor(it))
            binding?.txtLeftDirection?.text = it.endPoint[0]
            binding?.txtRightDirection?.text = it.endPoint[1]
            lineNumbers = it.lineList.toTypedArray()
            binding?.recLine?.adapter = LineNumAdapter(lineNumbers,this)
        })

        // adapter과 연결
        binding?.recLine?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.recLine?.adapter = LineNumAdapter(lineNumbers,this)
        binding?.recLeft?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recLeft?.adapter = StationDataAdapter(apiModelList,this)
        binding?.recRight?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recRight?.adapter = StationDataAdapter(apiModelList,this)

        /*
        //뒤로 버튼 클릭시 이벤트
        binding?.btnBack?.setOnClickListener {
            mainActivity.binding.frgMain.visibility = View.INVISIBLE
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        }
         */

        // 왼쪽 역 버튼 클릭시 이벤트
        binding?.btnLeft?.setOnClickListener {
            val array: Array<String>? = viewModel.onCrossedLine("left")
            if(array == null)
                viewModel.gotoStation(Direction.LEFT)
            else{
                var index: Int
                val builder = AlertDialog.Builder(this.activity)
                builder
                    .setTitle("Selct Station")
                    .setItems(array){_, which->
                        Toast.makeText(this.activity, "${array[which]} is Selected",Toast.LENGTH_SHORT).show()
                        index = which
                        println("index : $index")
                        viewModel.gotoStation(Direction.LEFT, index)
                    }.show()

            }
        }

        //오른쪽 역 버튼 클릭시 이벤트
        binding?.btnRight?.setOnClickListener {
            val array: Array<String>? = viewModel.onCrossedLine("right")
            if(array == null)
                viewModel.gotoStation(Direction.RIGHT)
            else{
                var index: Int
                val builder = AlertDialog.Builder(this.activity)
                builder
                    .setTitle("Selct Station")
                    .setItems(array){_, which->
                        Toast.makeText(this.activity, "${array[which]} is Selected",Toast.LENGTH_SHORT).show()
                        index = which
                        println("index : $index")
                        viewModel.gotoStation(Direction.RIGHT,index)
                    }.show()

            }
        }

        /*
        //알람 버튼 클릭시 이벤트
        binding?.btnAlarm?.setOnClickListener {
            viewModel.setAlarm()
        }
         */

        viewModel.onStationSelect("홍대입구")

        return binding?.root
    }

    // 메모리 낭비를 줄이기
    override fun onDestroy() {
        mainActivity.binding.frgMain.visibility = View.INVISIBLE
        super.onDestroy()
        binding = null
    }
    
    override fun changeLine(lineNum: Int) {
        viewModel.changeLine(lineNum)
    }

    override fun onBackPressed() {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    fun getColor(station: Station): Int {
        when(station.id/100) {
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
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    /** 알람을 설정했을 때 호출하는 callback 입니다.*/
    override fun onAlarmSet() {
        Toast.makeText(this.context, "알람이 설정되었습니다.", Toast.LENGTH_SHORT).show()
        //알람을 셋팅합니다.
        viewModel.setAlarm()
    }

}