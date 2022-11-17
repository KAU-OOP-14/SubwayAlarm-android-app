package com.example.subway_alarm

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.subway_alarm.ui.fragments.MainFragment
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.databinding.FragmentEntryBinding
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.ext.android.viewModel

class EntryFragment : Fragment() {
    private var isFabOpen = false // Fab 버튼으로 처음에 fasle로 초기화
    var binding : FragmentEntryBinding? = null
    private var open = false
    private val viewModel by viewModel<ViewModelImpl>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            open = it.getBoolean("open")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEntryBinding.inflate(inflater)
        /* 이런식으로 viewModel을 통해 input값을 알려줍니다
         모든 데이터 처리는 viewModel이 합니다 */

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.fabMain?.setOnClickListener{
            toggleFab()
        }
        binding?.fabSearch?.setOnClickListener(){
            findNavController().navigate(R.id.action_entryFragment_to_searchFragment)
            toggleFab()
        }
        binding?.fabBookmark?.setOnClickListener(){
            findNavController().navigate(R.id.action_entryFragment_to_bookmarkFragment)
            toggleFab()
        }
        binding?.fabSetting?.setOnClickListener(){
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
}