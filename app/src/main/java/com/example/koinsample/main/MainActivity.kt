package com.example.koinsample.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.koinsample.R
import com.example.koinsample.coroutine.ScopedAppActivity
import com.example.koinsample.databinding.ActivityMainBinding
import com.example.koinsample.repository.Controller
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ScopedAppActivity(), AnkoLogger {

    private val controller: Controller by inject()
    private val myViewModel: MainViewModel by viewModel()

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this@MainActivity,
            R.layout.activity_main
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            toast(controller.service.name)

            asyncShowCountDown()
            launch { showIOData() }
        }

        binding.viewModel = myViewModel

        myViewModel.number.observe(this, Observer {
            binding.viewModel = myViewModel
        })

        retrofitTestButton.setOnClickListener {
            myViewModel.getTodo(1) { todo ->
                toast(todo.toString())
            }
        }
    }

    private fun asyncShowCountDown() = launch(Dispatchers.Main) {
        for (i in 10 downTo 1) {
            countDownTextView.text = "카운트다운 $i ..."
            delay(500)
        }
        countDownTextView.text = "Done!"
    }

    private suspend fun showIOData() {
        val deferred = async(Dispatchers.IO) {
            // impl
            repeat(15) {
                delay(500)
                debug { "background 처리중..." }
            }
            100
        }
        withContext(Dispatchers.Main) {
            val data = deferred.await()
            // Show data in UI

            countDownTextView.text = "showIOData : $data"
        }
    }
}
