package com.example.subway_alarm.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subway_alarm.databinding.ActivitySearchBinding
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import com.example.subway_alarm.ui.adapter.SearchedListAdapter

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding

    var stationList: MutableList<Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stationList = Subway.lines[1].stations

        // Toolbar에 뒤로가기 버튼, Title 추가
        setSupportActionBar(binding.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("역검색")

        // 리사이컬뷰를 adapter와 연결
        binding.recStations.layoutManager = LinearLayoutManager(this)
        binding.recStations.adapter = SearchedListAdapter(stationList)

        val searchViewTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener{
                // 검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                override fun onQueryTextSubmit(s: String?): Boolean {
                    println("완료")
                    return false
                }

                //텍스트 입력/수정시에 호출
                override fun onQueryTextChange(s: String?): Boolean {
                    println("searchView is changed! :" + s)
                    return false
                }

            }
        // SearchView에 OnQueryTextLitener 부착
        binding.searchStation.setOnQueryTextListener(searchViewTextListener)

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


}