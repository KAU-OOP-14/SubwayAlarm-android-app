package com.example.subway_alarm

import android.app.Application
import android.content.Context
import com.example.subway_alarm.data.api.ApiThread
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.api.StationApiStorage
import com.example.subway_alarm.data.repository.StationRepository
import com.example.subway_alarm.data.repository.StationRepositoryImpl
import com.example.subway_alarm.models.ViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class SubwayAlarmApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule, appViewModule)
        }
    }
    init{
        instance = this
    }

    companion object {
        lateinit var instance: SubwayAlarmApp

        /**
         * ApplicationContext가 필요할 때 호출하는 static 함수입니다.
         */
        fun ApplicationContext() : Context {
            return instance.applicationContext
        }
    }


}

/** 싱글톤 의존성 주입 */
val appModule = module {
    single<StationApi> { StationApiStorage() }
    single<StationRepository> {
        println("역 저장소 생성")
        StationRepositoryImpl(get())
    }

    // thread
    factory {
        ApiThread(get())
    }

}


/** viewModel 의존성 주입 */
val appViewModule = module {
    viewModel {
        ViewModelImpl(get(), get())
    }
}