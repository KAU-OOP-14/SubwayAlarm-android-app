package com.example.subway_alarm.di

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.subway_alarm.R
import com.example.subway_alarm.model.api.service.NetworkService
import com.example.subway_alarm.model.repository.FirebaseRepository
import com.example.subway_alarm.model.repository.FirebaseRepositoryImpl
import com.example.subway_alarm.model.repository.StationRepository
import com.example.subway_alarm.model.repository.StationRepositoryImpl
import com.example.subway_alarm.service.AlarmService
import com.example.subway_alarm.ui.activities.MainActivity
import com.example.subway_alarm.ui.fragments.EntryFragment
import com.example.subway_alarm.viewModel.*
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/** 싱글톤 의존성 주입 */
val appModule = module {
    single<StationRepository> { StationRepositoryImpl(get()) }
    single<FirebaseRepository> { FirebaseRepositoryImpl(get())}
    single { FirebaseRepositoryImpl(get()) }
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://swopenapi.seoul.go.kr/")
            .build()
            .create(NetworkService::class.java)
    }

    single {
        FirebaseFirestore.getInstance()
    }
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