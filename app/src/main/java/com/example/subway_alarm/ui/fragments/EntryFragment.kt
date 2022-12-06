package com.example.subway_alarm.ui.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.R
import com.example.subway_alarm.databinding.FragmentEntryBinding
import com.example.subway_alarm.viewModel.PositionViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class EntryFragment : Fragment() {
    private val positionViewModel by sharedViewModel<PositionViewModel>()
    private lateinit var callback: OnBackPressedCallback
    private var isFabOpen = false // Fab 버튼으로 처음에 fasle로 초기화
    private var heightPixel: Float = 0f
    private var lastTimeBackPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수
    private var paramStationId = 0

    var binding: FragmentEntryBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramStationId = it.getInt("stationId")
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(isFabOpen)
                    toggleFab()
                else{
                    if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
                        activity?.finish()
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

        positionViewModel.setState(true) // activity에서의 onTouch 활성화
        binding?.stationImage?.scaleX = 4.0f
        binding?.stationImage?.scaleY = 4.0f
        heightPixel = positionViewModel.heightPixels.toFloat()

        if (paramStationId > 0) {
            val bottomSheet = MainFragment()
            val bundle = Bundle()
            // 프래그먼트 위에 그린 프래그먼트를 교체할 때는 childFragmentManager를 이용
            bundle.putInt("stationId", paramStationId)
            paramStationId = 0
            bottomSheet.arguments = bundle
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }

        binding?.fabMain?.setOnClickListener {
            toggleFab()
        }

        binding?.btnZoomIn?.setOnClickListener {
            positionViewModel.onZoomIn()
        }

        binding?.btnZoomOut?.setOnClickListener {
            positionViewModel.onZoomOut()
        }


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        positionViewModel.scaleValue.observe(viewLifecycleOwner) { scaleValue ->
            binding?.stationImage?.scaleX = scaleValue
            binding?.stationImage?.scaleY = scaleValue
        }

        // 항상 처음에 터치한 경우
        positionViewModel.pos.observe(viewLifecycleOwner) {
            if (isFabOpen) toggleFab()
        }

        positionViewModel.movePos.observe(viewLifecycleOwner) {
            binding?.stationImage?.translationX = positionViewModel.transValue.x
            binding?.stationImage?.translationY = positionViewModel.transValue.y
        }

        positionViewModel.selectedPos.observe(viewLifecycleOwner) {
            //something
        }

        positionViewModel.stationId.observe(viewLifecycleOwner) { stationId ->
            if (stationId != 0) {
                positionViewModel.setStationId(stationId)
                binding?.stationImage?.translationX = positionViewModel.transValue.x
                binding?.stationImage?.translationY = positionViewModel.transValue.y
                val bottomSheet = MainFragment()
                val bundle = Bundle()
                // 프래그먼트 위에 그린 프래그먼트를 교체할 때는 childFragmentManager를 이용
                bundle.putInt("stationId", stationId)
                positionViewModel.setStationId(0)
                bottomSheet.arguments = bundle
                bottomSheet.show(childFragmentManager, bottomSheet.tag)
            }
        }

        binding?.fabSearch?.setOnClickListener() {
            positionViewModel.setState(false)
            positionViewModel.resetTranslationValue()
            toggleFab()
            findNavController().navigate(R.id.action_entryFragment_to_searchFragment)
        }
        binding?.fabBookmark?.setOnClickListener() {
            positionViewModel.setState(false)
            positionViewModel.resetTranslationValue()
            toggleFab()
            findNavController().navigate(R.id.action_entryFragment_to_bookmarkFragment)
        }
        binding?.fabSetting?.setOnClickListener() {
            positionViewModel.setState(false)
            positionViewModel.resetTranslationValue()
            toggleFab()
            findNavController().navigate(R.id.action_entryFragment_to_settingFragment)
        }
    }

    // 메모리 관리를 위해서
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onDetach() {
        super.onDetach()
        // OnBackPressedCallBack 객체 제거
        callback.remove()
    }

    /** Floating action button를 isFabOpen변수의 condition에 따라 움직여줍니다 */
    private fun toggleFab(){
        if(isFabOpen){
            ObjectAnimator.ofFloat(binding?.fabSetting, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabBookmark, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSearch, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabMain, View.ROTATION,360f, 0f).apply { start() }
        }
        else{
            ObjectAnimator.ofFloat(binding?.fabSetting, "translationY", heightPixel/4f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabBookmark, "translationY", heightPixel/6f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabSearch, "translationY", heightPixel/12f).apply { start() }
            ObjectAnimator.ofFloat(binding?.fabMain, View.ROTATION,-360f, 0f).apply { start() }
        }

        isFabOpen = !isFabOpen
    }
}