package com.example.subway_alarm

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.subway_alarm.ui.fragments.MainFragment
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.databinding.FragmentEntryBinding
import com.example.subway_alarm.viewModel.PositionViewModel
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel

class EntryFragment : Fragment() {
    private val viewModel by viewModel<ViewModelImpl>()
    private val posViewModel: PositionViewModel by activityViewModels()
    private lateinit var callback: OnBackPressedCallback // 객체 선언
    private var isFabOpen = false // Fab 버튼으로 처음에 fasle로 초기화
    var binding : FragmentEntryBinding? = null
    var lastTimeBackPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수
    private var open = false
    var valueX = 0f
    var valueY = 0f
    var totalValueX = 0f
    var totalValueY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            open = it.getBoolean("open")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isFabOpen)
                    toggleFab()
                else{
                    if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
                        activity?.finish()
                    }
                    lastTimeBackPressed = System.currentTimeMillis()
                    Toast.makeText(binding?.root?.context, "'뒤로' 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // onBackPressedDispatcher에 등록해준다.
        // 이러면 OnBackPress()이벤트 발생시 BackPressedDispatcher에
        // 등록된 리스너들 중 생명주기의 상태가 Alive 상태의 콜백 리스너들만 실행
        // 단 main activity에서 onBackPressed함수를 override하면 안된다 ㅠ
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEntryBinding.inflate(inflater)
        /* 이런식으로 viewModel을 통해 input값을 알려줍니다
         모든 데이터 처리는 viewModel이 합니다 */
        posViewModel.setState(true)
        binding?.stationImage?.scaleX = 4.0f
        binding?.stationImage?.scaleY = 4.0f

        if(open) {
            open = false
            val bottomSheet = MainFragment()
            val bundle = Bundle()
            // 프래그먼트 위에 그린 프래그먼트를 교체할 때는 childFragmentManager를 이용
            bundle.putInt("stationId", viewModel.curStation.value.id)
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager,bottomSheet.tag)
        }

        binding?.btnStation?.setOnClickListener {
            //입력한 역의 api 요청
            val bottomSheet = MainFragment()
            // 프래그먼트 위에 그린 프래그먼트를 교체할 때는 childFragmentManager를 이용
            bottomSheet.show(childFragmentManager,bottomSheet.tag)
        }

        binding?.btnZoomIn?.setOnClickListener{
            val _scaleX = binding?.stationImage?.scaleX ?: 1.0f
            val _scaleY = binding?.stationImage?.scaleY ?: 1.0f
            if(_scaleX < 5.0f) {
                binding?.stationImage?.scaleX = _scaleX + 1f
                binding?.stationImage?.scaleY = _scaleY + 1f
                println("zoomin")
            }
        }

        binding?.btnZoomOut?.setOnClickListener{
            val _scaleX = binding?.stationImage?.scaleX ?: 1.0f
            val _scaleY = binding?.stationImage?.scaleY ?: 1.0f
            if(_scaleX > 1.0f) {
                binding?.stationImage?.scaleX = _scaleX - 1f
                binding?.stationImage?.scaleY = _scaleY - 1f
                println("zoomout")
            }
        }


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        posViewModel.pos.observe(viewLifecycleOwner){
            if(posViewModel.isMoving.value){
                println("moving end!")
                binding?.stationImage?.translationX = valueX - (posViewModel.tempPos.x - it.x)
                binding?.stationImage?.translationY = valueY - (posViewModel.tempPos.y - it.y)
                valueX -= (posViewModel.tempPos.x - it.x)
                valueY -= (posViewModel.tempPos.y - it.y)
                println("valueX: $valueX, valueY: $valueY")
                posViewModel.setMoving(false)
                val _scale = binding?.stationImage?.scaleX ?: 1.0f
                totalValueX -= ((posViewModel.tempPos.x - it.x) / _scale)
                totalValueY -= ((posViewModel.tempPos.y - it.y) / _scale)
            }
            else {
                println("station is selected!")
                val _scale = binding?.stationImage?.scaleX ?: 1.0f
                println("scale: $_scale")
                println("totalx : $totalValueX, totaly : $totalValueY")
                printReadPos(posViewModel.pos.value, _scale, totalValueX, totalValueY)
            }

        }

        posViewModel.movePos.observe(viewLifecycleOwner){
            println("moving~")
            binding?.stationImage?.translationX = valueX - (posViewModel.pos.value.x - it.x)
            binding?.stationImage?.translationY = valueY - (posViewModel.pos.value.y - it.y)
        }

        binding?.fabMain?.setOnClickListener{
            toggleFab()
        }
        binding?.fabSearch?.setOnClickListener(){
            posViewModel.setState(false)
            findNavController().navigate(R.id.action_entryFragment_to_searchFragment)
            toggleFab()
        }
        binding?.fabBookmark?.setOnClickListener(){
            posViewModel.setState(false)
            findNavController().navigate(R.id.action_entryFragment_to_bookmarkFragment)
            toggleFab()
        }
        binding?.fabSetting?.setOnClickListener(){
            posViewModel.setState(false)
            findNavController().navigate(R.id.action_entryFragment_to_settingFragment)
            toggleFab()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        // 메모리 관리를 위해서
        binding = null
    }

    private fun toggleFab(){
        if(isFabOpen){
            ObjectAnimator.ofFloat(binding?.fabSetting, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabBookmark, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSearch, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabMain, View.ROTATION,360f, 0f).apply { start() }
        }
        else{
            ObjectAnimator.ofFloat(binding?.fabSetting, "translationY", 540f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabBookmark, "translationY", 360f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSearch, "translationY", 180f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabMain, View.ROTATION,-360f, 0f).apply { start() }
        }

        isFabOpen = !isFabOpen
    }

    override fun onDetach() {
        super.onDetach()
        // OnBackPressedCallBack 객체 제거
        callback.remove()
    }

    // 확대 및 축소를 해도 원래 좌표값(scale이 1일 때)을 출력하도록 하는 함수
    fun printReadPos(pos: PointF, scale: Float, transX : Float = 0f, transY: Float = 0f){
        val resultPos = PointF(0f, 0f)
        //1080 * 2280
        val x = (pos.x / scale) + (1080 * (scale - 1) / (2 * scale) ) - transX
        val y = (pos.y / scale) + (2280 * (scale - 1) / (2 * scale) ) - transY
        println("x : $x, y : $y")
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EntryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}