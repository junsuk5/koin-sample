package com.example.koinsample.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.koinsample.model.Todo
import com.example.koinsample.repository.HelloRepository
import com.example.koinsample.repository.Webservice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(val repo: HelloRepository, val api: Webservice) : ViewModel() {
    private val disposable by lazy {
        CompositeDisposable()
    }

    val number = MutableLiveData<Int>()

    init {
        number.value = 0
    }

    fun increase() {
        number.value = number.value?.plus(1)
    }

    fun decrease() {
        number.value = number.value?.minus(1)
    }

    fun sayHello() = "${repo.giveHello()} from $this"

    fun getTodo(id: Int, completion: (todo: Todo) -> Unit) {
        disposable.add(
            api.getTodo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completion(it)
                }, {

                })
        )
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}