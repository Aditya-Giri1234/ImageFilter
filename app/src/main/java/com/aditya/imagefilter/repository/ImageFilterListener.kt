package com.aditya.imagefilter.repository

import com.aditya.imagefilter.model.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}