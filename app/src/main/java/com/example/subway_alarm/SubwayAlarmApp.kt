package com.example.subway_alarm

import android.app.Application
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

    /* 싱글톤 관련 의존성 주입 */
    val appModule = module {
        single<StationApi> { StationApiStorage() }
        single<StationRepository> { StationRepositoryImpl(get())}
    }

    /* viewModel 의존성 주입 */
    val appViewModule = module {
        viewModel {
            ViewModelImpl(get())
        }
    }
}