package com.example.subway_alarm.ui.fragments

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
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentEntryBinding
import com.example.subway_alarm.viewModel.PositionViewModel
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel

class EntryFragment : Fragment() {
    private val viewModel by viewModel<ViewModelImpl>()
    private val positionViewModel: PositionViewModel by activityViewModels()
    private lateinit var callback: OnBackPressedCallback // 객체 선언
    private var isFabOpen = false // Fab 버튼으로 처음에 fasle로 초기화
    var binding : FragmentEntryBinding? = null
    var lastTimeBackPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수
    private var open = false

    var valueX = 0f         // 한번 드래그 할 때마다 움직인 x값
    var valueY = 0f

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
        positionViewModel.setState(true)
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
            /*
            val _scaleX = binding?.stationImage?.scaleX ?: 1.0f
            val _scaleY = binding?.stationImage?.scaleY ?: 1.0f
            if(_scaleX < 4.0f) {
                binding?.stationImage?.scaleX = _scaleX + 2.0f
                binding?.stationImage?.scaleY = _scaleY + 2.0f
                println("zoomin")
            }
             */
        }

        binding?.btnZoomOut?.setOnClickListener{
            /*
            val _scaleX = binding?.stationImage?.scaleX ?: 1.0f
            val _scaleY = binding?.stationImage?.scaleY ?: 1.0f
            if(_scaleX > 2.0f) {
                binding?.stationImage?.scaleX = _scaleX - 2.0f
                binding?.stationImage?.scaleY = _scaleY - 2.0f
                println("zoomout")
            }

             */
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        positionViewModel.selectedPos.observe(viewLifecycleOwner){
            println("역선택한 경우!")
        }

        positionViewModel.stationId.observe(viewLifecycleOwner){
            println("넘어온 id값: $it")
            if(it != 0) {
                val bottomSheet = MainFragment()
                val bundle = Bundle()
                // 프래그먼트 위에 그린 프래그먼트를 교체할 때는 childFragmentManager를 이용
                bundle.putInt("stationId", it)
                bottomSheet.arguments = bundle
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }
        positionViewModel.pos.observe(viewLifecycleOwner){
            // 항상 처음에 터치한 경우
            if (isFabOpen){
                toggleFab()
            }
        }

        positionViewModel.movePos.observe(viewLifecycleOwner){
            if(positionViewModel.isMoving.value){
                binding?.stationImage?.translationX = valueX - positionViewModel.transValue.x
                binding?.stationImage?.translationY = valueY - positionViewModel.transValue.y
            }
            else{
                // 처음 터치한 좌표와 움직인 이후 손을 뗀 좌표 사이 거리를 계산한다
                val tranX = positionViewModel.transValue.x
                val tranY = positionViewModel.transValue.y
                binding?.stationImage?.translationX = valueX - tranX
                binding?.stationImage?.translationY = valueY - tranY
                valueX -= tranX
                valueY -= tranY
                println("valueX: $valueX, valueY: $valueY")
            }

        }

        binding?.fabMain?.setOnClickListener{
            toggleFab()
        }
        binding?.fabSearch?.setOnClickListener(){
            positionViewModel.setState(false)
            findNavController().navigate(R.id.action_entryFragment_to_searchFragment)
            toggleFab()
        }
        binding?.fabBookmark?.setOnClickListener(){
            positionViewModel.setState(false)
            findNavController().navigate(R.id.action_entryFragment_to_bookmarkFragment)
            toggleFab()
        }
        binding?.fabSetting?.setOnClickListener(){
            positionViewModel.setState(false)
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


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EntryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}