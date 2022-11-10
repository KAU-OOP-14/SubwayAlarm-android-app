package com.example.subway_alarm.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.FragmentMainBinding
import com.example.subway_alarm.databinding.FragmentNewNewBinding
import com.example.subway_alarm.model.api.dataModel.ApiModel
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.ui.adapter.LineNumAdapter
import com.example.subway_alarm.ui.adapter.StationDataAdapter
import com.example.subway_alarm.viewModel.OnLineChange
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel
import com.example.subway_alarm.viewModel.ViewModelImpl.Direction


class MainFragment : Fragment(), OnLineChange, MainActivity.onBackPressedListener {
    var binding: FragmentNewNewBinding? = null
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
        binding = FragmentNewNewBinding.inflate(inflater, container, false)
        mainActivity.binding.frgMain.visibility = View.VISIBLE

        /* View Model과 View 연결 */
        viewModel.apis.observe(viewLifecycleOwner, Observer {
            var data = ""
            apiModelList = it
            binding?.recLeft?.adapter = StationDataAdapter(apiModelList)
            for( model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
            /*
            binding?.txtLeftStationData?.text = data
            if(it.isEmpty()) {
                binding?.txtLeftStationData?.text = ""
            }

             */
        })

        // view model의 cur station 관찰
        viewModel.curStation.observe(viewLifecycleOwner, Observer {
            binding?.txtStationName?.text = it.stationName
            binding?.txtLeftStation?.text = it.leftStation?.stationName?:"역 정보 없음"
            binding?.txtRightStation?.text = it.rightStation?.stationName?:"역 정보 없음"
            lineNumbers = it.lineList.toTypedArray()
            binding?.recLine?.adapter = LineNumAdapter(lineNumbers,this)
        })

        // adapter과 연결
        binding?.recLine?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.recLine?.adapter = LineNumAdapter(lineNumbers,this)
        binding?.recLeft?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding?.recLeft?.adapter = StationDataAdapter(apiModelList)

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
        super.onDestroy()
        binding = null
    }
    
    override fun changeLine(lineNum: Int) {
        viewModel.changeLine(lineNum)
    }

    override fun onBackPressed() {
        mainActivity.binding.frgMain.visibility = View.INVISIBLE
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}