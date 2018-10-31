package com.example.koinsample.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koinsample.R
import com.example.koinsample.coroutine.ScopedAppActivity
import com.example.koinsample.databinding.ActivityMainBinding
import com.example.koinsample.databinding.ItemRecyclerBinding
import com.example.koinsample.model.DummyData
import com.example.koinsample.repository.Controller
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ScopedAppActivity() {

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

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MyAdapter(listOf(DummyData(1), DummyData(2), DummyData(3))) { data, pos ->
                toast("$data : $pos")
            }
        }
    }

    class MyAdapter(private val myDataset: List<DummyData>,
                    val onItemClickListener: (data: DummyData, pos: Int) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        class MyViewHolder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recycler, parent, false)
            return MyViewHolder(ItemRecyclerBinding.bind(view))
        }

        override fun getItemCount() = myDataset.size

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.binding.data = myDataset[position]
            holder.binding.root.setOnClickListener {
                onItemClickListener.invoke(myDataset[position], position)
            }
        }
    }

    // 기본 Main Thread (ScopedAppActivity)
    private fun asyncShowCountDown() = launch {
        for (i in 10 downTo 1) {
            countDownTextView.text = "카운트다운 $i ..."
            delay(500)
        }
        countDownTextView.text = "Done!"
    }

    private suspend fun showIOData() {
        // Background Thread
        val deferred = async(Dispatchers.IO) {
            // impl
            repeat(15) {
                delay(500)
                debug { "background 처리중..." }
            }
            100
        }
        // UI Thread
        withContext(Dispatchers.Main) {
            val data = deferred.await()
            // Show data in UI

            countDownTextView.text = "showIOData : $data"
        }
    }
}
