package com.example.subway_alarm.module

import com.example.subway_alarm.data.api.ApiThread
import com.example.subway_alarm.data.api.StationApi
import com.example.subway_alarm.data.api.StationApiStorage
import com.example.subway_alarm.data.api.service.ApiService
import com.example.subway_alarm.data.api.service.ApiServiceImpl
import com.example.subway_alarm.data.repository.StationRepository
import com.example.subway_alarm.data.repository.StationRepositoryImpl
import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** 싱글톤 의존성 주입 */
val appModule = module {
    single<StationApi> { StationApiStorage() }
    single<StationRepository> { StationRepositoryImpl(get()) }
    single<ApiService> { ApiServiceImpl() }

    // thread
    factory { ApiThread(get()) }

}


/** viewModel 의존성 주입 */
val appViewModule = module {
    viewModel { ViewModelImpl(get(), get(), get()) }

}
