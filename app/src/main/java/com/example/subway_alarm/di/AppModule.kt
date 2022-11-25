package com.example.subway_alarm.di

import com.example.subway_alarm.model.repository.FirebaseRepository
import com.example.subway_alarm.model.repository.StationPositionRepository
import com.example.subway_alarm.model.repository.StationRepository
import com.example.subway_alarm.model.repository.StationRepositoryImpl
import com.example.subway_alarm.viewModel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** 싱글톤 의존성 주입 */
val appModule = module {
    single<StationRepository> { StationRepositoryImpl() }
    single<FirebaseRepository> { StationPositionRepository()}
    single { StationPositionRepository() }
}

/** viewModel 의존성 주입 */
val appViewModule = module {
    viewModel {
        ArrivalViewModel(get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        BookmarkViewModel(get())
    }
    viewModel {
        AlarmViewModel()
    }
    viewModel {
        PositionViewModel(get())
    }
}