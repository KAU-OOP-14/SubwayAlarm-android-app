package com.example.subway_alarm.viewModel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel : ViewModel() {
    // rxjava로 구독하고 있는 disposable 입니다.
    // onCleared 할 때, 메모리에서 해제해야 합니다.
    private val disposables = io.reactivex.rxjava3.disposables.CompositeDisposable()

    fun addToDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}