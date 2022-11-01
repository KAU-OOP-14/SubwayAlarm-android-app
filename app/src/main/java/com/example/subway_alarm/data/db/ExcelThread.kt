package com.example.subway_alarm.data.db

import com.example.subway_alarm.SubwayAlarmApp
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ExcelThread(): Thread() {
    private val db: MutableList<Array<String>> = mutableListOf()

    override fun run() {
        try {
            val context = SubwayAlarmApp.ApplicationContext()
            val myFileSystem = context.resources.assets.open("stationdata.xlsx")
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
                db.add(item)
            }
        } catch (e: Exception) {
            println("엑셀 파일을 읽지 못했습니다.")
        }

    }

    fun getdb(): MutableList<Array<String>> {
        return db
    }

}

