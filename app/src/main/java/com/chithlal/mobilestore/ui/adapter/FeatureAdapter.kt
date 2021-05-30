package com.chithlal.mobilestore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chithlal.mobilestore.R
import com.chithlal.mobilestore.databinding.AdapterOptionLayoutBinding
import com.chithlal.mobilestore.model.Option

class FeatureAdapter (val context: Context, val featureList: List<Option>,val listener: FeatureClickListener):
    RecyclerView.Adapter<FeatureAdapter.ViewHolder>() {

    private val selectedFeatureIdList = ArrayList<String>()
    private val selectedFeatureList = ArrayList<Option>()

    class ViewHolder(binding: AdapterOptionLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        val image = binding.imgOption
        val name = binding.tvName
        val rootView = binding.root

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_option_layout,parent,false)
        val binding = AdapterOptionLayoutBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val feature = featureList[position]

        if(selectedFeatureIdList.contains(feature.id)){
            holder.rootView.background = ContextCompat.getDrawable(context,R.drawable.background_selected)
        }
        else{
            holder.rootView.background = ContextCompat.getDrawable(context,R.drawable.background_unselected)
        }

        feature.icon?.let {
            Glide.with(context)
                .load(it)
                .into(holder.image)
        }
        holder.name.text = feature.name

        holder.rootView.setOnClickListener{
            if (selectedFeatureIdList.contains(feature.id)) {
                selectedFeatureIdList.remove(feature.id)
                selectedFeatureList.remove(feature)
            }
            else {
                selectedFeatureIdList.add(feature.id)
                selectedFeatureList.add(feature)
            }

            listener.onClick(selectedFeatureList)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return featureList.size
    }
    interface FeatureClickListener{
        fun onClick(optionList: ArrayList<Option>)
    }
}