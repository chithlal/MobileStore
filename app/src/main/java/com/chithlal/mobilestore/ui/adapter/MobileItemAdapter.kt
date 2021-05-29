package com.chithlal.mobilestore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chithlal.mobilestore.R
import com.chithlal.mobilestore.databinding.AdapterMobileLayoutBinding
import com.chithlal.mobilestore.model.Option

class MobileItemAdapter(val context: Context,var mobileList: List<Option>,val listener: MobileClickListener):
    RecyclerView.Adapter<MobileItemAdapter.ViewHolder>() {
    class ViewHolder(binding: AdapterMobileLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        val name = binding.tvPhoneName
        val icon = binding.imgMobilePhone
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_mobile_layout,parent,false)
        val adapterMobileLayoutBinding = AdapterMobileLayoutBinding.bind(view)
        return ViewHolder(adapterMobileLayoutBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mobile = mobileList[position]
        //load image with glide
        mobile.icon?.let {
            Glide.with(context)
                .load(mobile.icon)
                .into(holder.icon)
        }

        holder.name.text = mobile.name

        holder.itemView.setOnClickListener{
            listener.onClick(mobile)
        }


    }
    fun updatePhones(data: List<Option>){
        mobileList = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return mobileList.size
    }

    interface MobileClickListener{
        fun onClick(option: Option)
    }
}