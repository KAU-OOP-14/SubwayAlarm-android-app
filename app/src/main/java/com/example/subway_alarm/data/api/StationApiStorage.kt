package com.example.subway_alarm.data.api

import androidx.lifecycle.ViewModel
import com.example.subway_alarm.models.ViewModelImpl
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class StationApiStorage() : StationApi {

    var apiStringData: String? = null
    val parsedApiData: Array<String?> = arrayOfNulls<String>(8)

    override fun getApiData(): Array<String?> {
        return parsedApiData
    }

    /** 전달받은 station의 이름으로 api 데이터를 요청하고
     * 파싱되지 않은 String 결과를 반환합니다. */
    override fun setStringApiData(stringApiData: String){
        apiStringData = stringApiData

    }

    /** 전달받은 string 타입의 api 데이터를 파싱합니다.
     * 파싱한 데이터를 Array<String?>으로 반환합니다.
     */
    override fun setParseApiData(apiStringData: String?) {
        // xml string 데이터를 document로 변환하고 파싱하기
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val document: Document = builder.parse(InputSource(StringReader(apiStringData)))

        // 전달받는 열차 하나의 데이터 리스트
        // item 값을 증가시켜서 다른 열차의 데이터를 받을 수 있음

        //데이터를 못 받아올 경우의 예외처리
        try {
            val lineId: String =
                document.getElementsByTagName("subwayId").item(0).getChildNodes().item(0).getNodeValue()
            val trainNo: String =
                document.getElementsByTagName("btrainNo").item(0).getChildNodes().item(0).getNodeValue()
            val destDirection: String =
                document.getElementsByTagName("trainLineNm").item(0).getChildNodes().item(0)
                    .getNodeValue()
            val destStationName: String =
                document.getElementsByTagName("bstatnNm").item(0).getChildNodes().item(0).getNodeValue()
            val arrivalCode: String =
                document.getElementsByTagName("arvlCd").item(0).getChildNodes().item(0).getNodeValue()
            val arrivalTime: String =
                document.getElementsByTagName("barvlDt").item(0).getChildNodes().item(0).getNodeValue()
            val innerOuter: String =
                document.getElementsByTagName("updnLine").item(0).getChildNodes().item(0).getNodeValue()
            val receivedTime: String =
                document.getElementsByTagName("recptnDt").item(0).getChildNodes().item(0).getNodeValue()

            parsedApiData[0] = lineId
            parsedApiData[1] = trainNo
            parsedApiData[2] = destDirection
            parsedApiData[3] = destStationName
            parsedApiData[4] = arrivalCode
            parsedApiData[5] = arrivalTime
            parsedApiData[6] = innerOuter
            parsedApiData[7] = receivedTime
        }catch(npe: NullPointerException) {
            println("데이터가 없습니다")
        }





    }
}

