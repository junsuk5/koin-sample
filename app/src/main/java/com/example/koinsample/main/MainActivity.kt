package com.example.koinsample.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.koinsample.R
import com.example.koinsample.databinding.ActivityMainBinding
import com.example.koinsample.repository.Controller
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), AnkoLogger {

    private val controller: Controller by inject()
    private val myViewModel: MainViewModel by viewModel()

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this@MainActivity,
            R.layout.activity_main
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            toast(controller.service.name)
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
}
