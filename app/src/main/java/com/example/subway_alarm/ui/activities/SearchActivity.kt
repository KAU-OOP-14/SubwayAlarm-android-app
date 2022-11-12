package com.example.subway_alarm.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.ActivitySearchBinding
import com.example.subway_alarm.di.SubwayAlarmApp.Companion.ApplicationContext
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.ext.android.inject
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.ui.adapter.SearchedListAdapter
import com.example.subway_alarm.viewModel.listener.OnSearchResultClick

class SearchActivity : AppCompatActivity(), OnSearchResultClick {
    val searchActivity: SearchActivity = this
    lateinit var binding: ActivitySearchBinding

    private val viewModel by inject<ViewModelImpl>()

    var stationList: MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        stationList = Subway.lines[0].stations

        // Toolbar에 뒤로가기 버튼, Title 추가
        setSupportActionBar(binding.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("역검색")

        // 리사이컬뷰를 adapter와 연결
        binding.recStations.layoutManager = LinearLayoutManager(this)
        binding.recStations.adapter = SearchedListAdapter(stationList, this)

        val searchViewTextListener: SearchView.OnQueryTextListener =
             object : SearchView.OnQueryTextListener{
                // 검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String?): Boolean {
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String?): Boolean {
                    s?.let {
                        binding.recStations.adapter = SearchedListAdapter(viewModel.onSearchTextChanged(s), searchActivity)
                    }
                    return false
                }

            }
        // SearchView에 OnQueryTextLitener 부착
        binding.searchStation.setOnQueryTextListener(searchViewTextListener)

        setContentView(binding.root)

    }

    //** Search Activity에서 뒤로가기 버튼 구현 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSearchResultClick(stationId: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("clickedStationId", stationId)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
        startActivity(intent)
    }

}