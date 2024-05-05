package com.aditya.imagefilter.adapter

import android.media.RouteListingPreference.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aditya.imagefilter.R
import com.aditya.imagefilter.databinding.SampleItemContainerFilterBinding
import com.aditya.imagefilter.model.ImageFilter
import com.aditya.imagefilter.repository.ImageFilterListener
import javax.inject.Inject

class ImageFilterAdapter @Inject constructor(private val imageFilterListener: ImageFilterListener) :
    RecyclerView.Adapter<ImageFilterAdapter.ViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = -1
    private val list = ArrayList<ImageFilter>()

    fun submitList(list: ArrayList<ImageFilter>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: SampleItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SampleItemContainerFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            list[position].let { data ->
                imageFilterPreview.setImageBitmap(data.filterPreview)
                imageFilerName.text = data.name
                root.setOnClickListener {
                    if (selectedFilterPosition!=position){
                        imageFilterListener.onFilterSelected(data)
                        previouslySelectedPosition=selectedFilterPosition
                        selectedFilterPosition = position
                        notifyItemChanged(previouslySelectedPosition, Unit)
                        notifyItemChanged(selectedFilterPosition, Unit)

                    }

                }
            }
            imageFilerName.setTextColor(
                ContextCompat.getColor(
                    imageFilerName.context,
                    if (selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )
        }
    }
}