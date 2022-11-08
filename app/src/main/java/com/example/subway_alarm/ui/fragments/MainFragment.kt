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
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.ui.adapter.LineNumAdapter
import com.example.subway_alarm.viewModel.OnLineChange
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel
import com.example.subway_alarm.viewModel.ViewModelImpl.Direction


class MainFragment : Fragment(), OnLineChange {
    var binding: FragmentMainBinding? = null
    val viewModel: ViewModelImpl by viewModel()
    var lineNumbers: Array<Int> = arrayOf()
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
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        mainActivity.binding.frgMain.visibility = View.VISIBLE
        /* View Model과 View 연결 */
        viewModel.apis.observe(viewLifecycleOwner, Observer {
            var data = ""

            for( model in it) {
                data += "${model.bstatnNm}|${model.trainLineNm}|${model.arvlMsg2}\n"
            }
            binding?.txtStationData?.text = data
            if(it.isEmpty()) {
                binding?.txtStationData?.text = ""
            }
        })

        viewModel.curStation.observe(viewLifecycleOwner, Observer {
            binding?.txtStationName?.text = it.stationName
            binding?.txtLeftStation?.text = it.leftStation?.stationName?:"역 정보 없음"
            binding?.txtRightStation?.text = it.rightStation?.stationName?:"역 정보 없음"
            lineNumbers = it.lineList.toTypedArray()
            binding?.recLineNum?.adapter = LineNumAdapter(lineNumbers,this)
        })

        // adapter과 연결
        binding?.recLineNum?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding?.recLineNum?.adapter = LineNumAdapter(lineNumbers,this)

        //뒤로 버튼 클릭시 이벤트
        binding?.btnBack?.setOnClickListener {
            mainActivity.binding.frgMain.visibility = View.INVISIBLE
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        }

        // 왼쪽 역 클릭시 이벤트
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

        //오른쪽 역 클릭시 이벤트
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

        //알람 버튼 클릭시 이벤트
        binding?.btnAlarm?.setOnClickListener {
            viewModel.setAlarm()
        }

        viewModel.onStationSelect("홍대입구")

        return binding?.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun changeLine(lineNum: Int) {
        viewModel.changeLine(lineNum)
    }

}