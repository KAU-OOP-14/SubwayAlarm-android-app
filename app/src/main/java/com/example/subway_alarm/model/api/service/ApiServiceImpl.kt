package com.example.subway_alarm.model.api.service

import com.example.subway_alarm.model.api.dataModel.ApiModelList

class ApiServiceImpl: ApiService {
    /*
    var storedApiList: ApiModelList = ApiModelList(null, listOf(ApiModel()))
    //retrofit 객체 생성 / 한번만 실행하면 됩니다.
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://swopenapi.seoul.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    var networkService: NetworkService = retrofit.create(NetworkService::class.java)

    //여기에 역 이름을 전달하면 ApiModelList라는 객체를 생성해서 modelList에 전달해줍니다.
    override fun requestApi(stationName: String) {
        val apiCallBack = networkService.doGetUserList(stationName)
        apiCallBack.enqueue(object: Callback<ApiModelList> {
            override fun onResponse(call: Call<ApiModelList>,
                                    response: Response<ApiModelList>
            ) {
                if(response.isSuccessful)
                    println("통신 성공")
                val data = response.body()
                if(data != null)
                    storedApiList = data
                println("저장된 data : $data")
            }
            override fun onFailure(call: Call<ApiModelList>, t: Throwable) {
                println(t.message)
                println("통신 실패")
                call.cancel()
            }
        })
    }


    override fun getApiModelList(): ApiModelList {
            return storedApiList
    }

     */
    override fun requestApi(stationName: String) {
        TODO("Not yet implemented")
    }

    override fun getApiModelList(): ApiModelList {
        TODO("Not yet implemented")
    }
}