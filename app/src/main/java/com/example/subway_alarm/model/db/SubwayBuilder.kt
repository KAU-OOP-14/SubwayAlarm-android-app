package com.example.subway_alarm.model.db

import com.example.subway_alarm.di.SubwayAlarmApp
import com.example.subway_alarm.model.Line
import com.example.subway_alarm.model.Station
import com.example.subway_alarm.model.Subway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object SubwayBuilder {
    var loading: Boolean = true

    /**
     * coroutine으로 Subway를 만들어줍니다.
     * Station Repository가 생성될 때 한번만 호출되는 함수입니다.
     */
    fun initSubway(): Subway{
        val lines: MutableList<Line> = mutableListOf()
        //Excel Thread에서 데이터를 추출합니다.
        val flow: Flow<Array<String>> = flow {
            //16개 line을 생성합니다.
            for(lineId in 1..16) {
                lines.add(Line(lineId))
            }

            try {
                val context = SubwayAlarmApp.ApplicationContext()
                val myFileSystem = context.resources.assets.open("stationdata_new.xlsx")
                val myWorkBook = XSSFWorkbook(myFileSystem)
                val sheet = myWorkBook.getSheetAt(0)

                val rowIterator = sheet.rowIterator()
                while(rowIterator.hasNext()) {
                    val myRow = rowIterator.next() as XSSFRow
                    val colIterator = myRow.cellIterator()

                    val item = arrayOf<String>("","","")
                    var colCount = 0
                    while(colIterator.hasNext()) {
                        val myCell = colIterator.next() as XSSFCell
                        item[colCount] = myCell.toString()
                        colCount++
                    }
                    emit(item)
                }
                println("로딩 완료...")
                /**
                 * 로딩완료 구현 필요
                 */
                loading = false
                //station이 모두 들어간 뒤, line을 subway에 넣습니다.
                for(line in lines) {
                    Subway.addLines(line)
                }
            } catch (e: Exception) {
                println("엑셀 파일을 읽지 못했습니다.")
            }
        }

        val lifecycleScope = CoroutineScope(Dispatchers.IO)
        lifecycleScope.launch {

            flow.collect() { row->
                val id: Int = row[1].substringBefore(".").toInt()
                val linelist: MutableList<Int> = mutableListOf()
                if(row[0].contains(".")){
                    linelist.add(row[0].substringBefore('.').toInt())
                }
                else{
                    val tempList = row[0].split(",")
                    for (i in tempList){
                        linelist.add(i.toInt())
                    }
                }
                lines[(id/100)-1].addStations(Station(row[2], id, linelist))
            }
        }

        return Subway
    }
}