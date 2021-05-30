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

class StorageOptionAdapter(val context: Context, val storageList: List<Option>,val listener: StorageClickListener):
    RecyclerView.Adapter<StorageOptionAdapter.ViewHolder>() {

    private var selectedStorageId = "nill"

    class ViewHolder(binding: AdapterOptionLayoutBinding):RecyclerView.ViewHolder(binding.root) {

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
        val storage = storageList[position]

        if(selectedStorageId == storage.id  ){
            holder.rootView.background = ContextCompat.getDrawable(context,R.drawable.background_selected)
        }
        else{
            holder.rootView.background = ContextCompat.getDrawable(context,R.drawable.background_unselected)
        }

        storage.icon?.let {
            Glide.with(context)
                .load(it)
                .into(holder.image)
        }
        holder.name.text = storage.name

        holder.rootView.setOnClickListener{
            if (selectedStorageId == storage.id )
                selectedStorageId = "nill"
            else
            selectedStorageId = storage.id

            listener.onClick(storage)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    interface StorageClickListener{
        fun onClick(option: Option)
    }
    }