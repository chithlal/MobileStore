package com.chithlal.mobilestore.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chithlal.mobilestore.R
import com.chithlal.mobilestore.databinding.ActivityFinalBinding
import com.chithlal.mobilestore.model.Option
import com.chithlal.mobilestore.ui.adapter.FeatureAdapter
import com.chithlal.mobilestore.ui.adapter.StorageOptionAdapter

const val PARAM_SELECTED_PHONE = "phone"
const val PARAM_SELECTED_STORAGE = "storage"
const val PARAM_SELECTED_FEATURE = "features"
class FinalActivity : AppCompatActivity() {



    private lateinit var selectedPhone: Option
    private lateinit var selectedStorageList: ArrayList<Option>
    private lateinit var selectedFeatureList: ArrayList<Option>

    private lateinit var mBinding: ActivityFinalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        intent?.let {
            selectedPhone = it.getSerializableExtra(PARAM_SELECTED_PHONE) as Option
            selectedStorageList = it.getSerializableExtra(PARAM_SELECTED_STORAGE) as ArrayList<Option>
            selectedFeatureList = it.getSerializableExtra(PARAM_SELECTED_FEATURE) as ArrayList<Option>

        }

        setupViews()
    }

    private fun setupViews() {

        selectedPhone?.let {
            mBinding.tvPhoneName.text = selectedPhone.name

            Glide.with(this)
                .load(selectedPhone.icon)
                .into(mBinding.imgMobilePhone)
        }

        if (!selectedStorageList.isNullOrEmpty()){
            mBinding.rvStorage.apply {
                layoutManager = LinearLayoutManager(this@FinalActivity,LinearLayoutManager.HORIZONTAL,false)
                adapter = StorageOptionAdapter(this@FinalActivity,selectedStorageList,null)
            }
        }
        if (!selectedFeatureList.isNullOrEmpty()){
            mBinding.rvOther.apply {
                layoutManager = LinearLayoutManager(this@FinalActivity)
                adapter = FeatureAdapter(this@FinalActivity,selectedFeatureList,null)
            }
        }
    }
}