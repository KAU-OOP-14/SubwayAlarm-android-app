package com.example.subway_alarm.data.api

class StationApiStorage: StationApi {
    /*
    val key: String = "7a5370504e6868743131324443656265"
    val type: String = "xml"
    val service: String = "realtimeStationArrival"
    val startIndex: String = "0"
    val endIndex: String = "5"
    val stationName: String = "서울"
    val url: String = "http://swopenAPI.seoul.go.kr/api/subway/$key/$type/$service/$startIndex/$endIndex/$stationName"

    Thread {
        val urlBuilder = StringBuilder("http://swopenAPI.seoul.go.kr/api/subway/7a5370504e6868743131324443656265/xml/realtimeStationArrival/0/5/서울") /*URL*/
        val url = URL(urlBuilder.toString())
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-type", "application/xml")
        println("Response code: " + conn.responseCode.toString()) /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        val rd: BufferedReader = if (conn.responseCode in 200..300) {
            BufferedReader(InputStreamReader(conn.inputStream))
        } else {
            BufferedReader(InputStreamReader(conn.errorStream))
        }
        val sb = StringBuilder()
        var line: String?
        while (rd.readLine().also { line = it } != null) {
            sb.append(line)
        }
        rd.close()
        conn.disconnect()
        println(sb.toString())
        text += sb.toString()

    }.start()

     */

    override fun getStationApiData(stationName: String): String {
        return "this is a mvvm test"
    }
}