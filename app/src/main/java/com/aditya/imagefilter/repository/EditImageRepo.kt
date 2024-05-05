package com.aditya.imagefilter.repository

import android.graphics.Bitmap
import android.net.Uri
import com.aditya.imagefilter.model.ImageFilter

interface EditImageRepo {
    suspend fun prepareImagePreview(imageUri: Uri) : Bitmap?
    suspend fun getImageFilters(image:Bitmap):List<ImageFilter>
    suspend fun saveFilteredImage(filterImage:Bitmap) : Uri?
}