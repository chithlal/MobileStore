package com.chithlal.mobilestore.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chithlal.mobilestore.databinding.ActivityMainBinding
import com.chithlal.mobilestore.model.Exclusion
import com.chithlal.mobilestore.model.Features
import com.chithlal.mobilestore.model.Option
import com.chithlal.mobilestore.ui.adapter.MobileItemAdapter
import com.chithlal.mobilestore.ui.viewmodel.StoreViewModel
import com.chithlal.mobilestore.utils.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var rowArray: Array<Int>
    lateinit var columnArray: Array<Int>
    lateinit var graph: Array<IntArray>
    lateinit var phones: List<Option>
    lateinit var storages: List<Option>
    lateinit var others: List<Option>
    lateinit var exclusions: List<List<Exclusion>>

    val idMap = HashMap<String, Int>()

    lateinit var mobileAdapter: MobileItemAdapter

    private lateinit var mainBinding: ActivityMainBinding
    @Inject()
    lateinit var networkUtil: NetworkUtil
    val storeViewModel: StoreViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setupObserver()

    }

    override fun onResume() {
        super.onResume()
        storeViewModel.getStoreData()
    }

    //observe live data for changes
    private fun setupObserver() {
        storeViewModel.storeLiveData.observe(this, Observer {
            Log.d("TAG", "setupObserver: ${it.features[0].name}")
            prepareGraph(it)
        })

        storeViewModel.storeErrorLiveData.observe(this, Observer {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun prepareGraph(features: Features) {

        // create separate list of features
        features.features.forEach {
            when (it.name) {
                "Mobile Phone" -> {
                    phones = it.options
                }
                "Storage Options" -> {
                    storages = it.options
                }
                "Other features" -> {
                    others = it.options
                }
            }
        }
        setAdapter(phones)
        exclusions = features.exclusions

        //create map of option id vs index to create graph vertex
        var index = 0 // graph vertex
        phones.forEach {
            idMap[it.id] = index
            index++
        }
        storages.forEach {
            idMap[it.id] = index
            index++
        }
        others.forEach {
            idMap[it.id] = index
            index++
        }

        val vertexCount = idMap.size
        rowArray = Array(vertexCount) { _ -> 1 }
        columnArray = Array(vertexCount) { _ -> 1 }
        graph = Array(vertexCount){IntArray(vertexCount){ 1} }

        exclusions.forEach {

            val vertexOne = it[0].options_id
            val vertexTwo = it[1].options_id
            graph[idMap[vertexOne]!!][idMap[vertexTwo]!!] = 0

        }

        for (row in graph) {
            Log.d("graph", row.contentToString())

        }

    }

    private fun setAdapter(phones: List<Option>) {
        mobileAdapter = MobileItemAdapter(this@MainActivity,phones,object : MobileItemAdapter.MobileClickListener{
            override fun onClick(option: Option) {

            }

        })
        mainBinding.rvMobiles.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mobileAdapter
        }
    }
}