package com.aditya.imagefilter.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aditya.imagefilter.databinding.SampleSavedImageBinding
import com.aditya.imagefilter.repository.SavedImageListener
import java.io.File
import javax.inject.Inject

class SavedImageAdapter @Inject constructor(val savedImageListener: SavedImageListener) : RecyclerView.Adapter<SavedImageAdapter.ViewHolder>() {

    private val list=ArrayList<Pair<File,Bitmap>>()

    fun  submitList(list:List<Pair<File,Bitmap>>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SampleSavedImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SampleSavedImageBinding.inflate(LayoutInflater.from(parent.context) ,parent,false))
    }

    override fun getItemCount(): Int {
       return this.list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.list[position].let {data->
            holder.binding.apply {
                ivSavedImage.setImageBitmap(data.second)
                ivSavedImage.setOnClickListener {
                    savedImageListener.onImageClicked(data.first)
                }
            }

        }

    }
}