package com.example.subway_alarm

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.example.subway_alarm.ui.fragments.MainFragment
import androidx.navigation.fragment.findNavController
import com.example.subway_alarm.databinding.FragmentEntryBinding
import com.example.subway_alarm.ui.activities.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EntryFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback // 객체 선언
    private var isFabOpen = false // Fab 버튼으로 처음에 fasle로 초기화
    var binding : FragmentEntryBinding? = null
    var lastTimeBackPressed = 0L  // 두 번 뒤로가기 버튼 눌려서 앱 종료하기 위한 변수


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}