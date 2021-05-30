package com.chithlal.mobilestore.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chithlal.mobilestore.databinding.ActivityMainBinding
import com.chithlal.mobilestore.model.Features
import com.chithlal.mobilestore.model.Option
import com.chithlal.mobilestore.ui.DetailsFragment
import com.chithlal.mobilestore.ui.adapter.MobileItemAdapter
import com.chithlal.mobilestore.ui.viewmodel.StoreViewModel
import com.chithlal.mobilestore.utils.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var phones: List<Option>

    private var features: Features? = null


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
        storeViewModel.getStoreData(this)
    }

    //observe live data for changes
    private fun setupObserver() {
        storeViewModel.storeLiveData.observe(this, Observer {
            Log.d("TAG", "setupObserver: ${it.features[0].name}")
            preparePhoneList(it)
        })

        storeViewModel.storeErrorLiveData.observe(this, Observer {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
        })
    }

    private fun preparePhoneList(features: Features) {

        this.features = features
        // create separate list of features
        features.features.forEach {
            when (it.name) {
                "Mobile Phone" -> {
                    phones = it.options
                }

            }
        }
        setAdapter(phones)


    }

    private fun setAdapter(phones: List<Option>) {
        mobileAdapter = MobileItemAdapter(this@MainActivity,phones,object : MobileItemAdapter.MobileClickListener{
            override fun onClick(option: Option) {
                if (features != null && option != null) {
                    val detailsBottomSheet = DetailsFragment.newInstance(option, features!!)
                    detailsBottomSheet.show(supportFragmentManager,"")
                }
            }

        })
        mainBinding.rvMobiles.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mobileAdapter
        }
    }
}