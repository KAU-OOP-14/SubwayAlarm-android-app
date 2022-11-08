package com.example.subway_alarm.di

import com.example.subway_alarm.viewModel.ViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** 싱글톤 의존성 주입 */
val appModule = module {
    //single<StationRepository> { StationRepositoryImpl(get())}


}


/** viewModel 의존성 주입 */
val appViewModule = module {
    viewModel {
        ViewModelImpl()
    }
}
