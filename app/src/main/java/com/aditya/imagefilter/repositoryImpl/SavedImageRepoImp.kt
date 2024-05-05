package com.aditya.imagefilter.repositoryImpl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.aditya.imagefilter.repository.SavedImageRepo
import com.aditya.imagefilter.util.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class SavedImageRepoImp @Inject constructor(@ApplicationContext val context: Context) : SavedImageRepo {
    override suspend fun loadSavedImage(): List<Pair<File, Bitmap>>? {
        val savedImages = ArrayList<Pair<File, Bitmap>>()

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constant.IMAGE_PATH
        )
        return dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImages.add(Pair(file, getPreviewBitmap(file)))
            }
            savedImages
        }

    }

    private fun getPreviewBitmap(file: File): Bitmap {
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val width = 150
        val height = ((originalBitmap.height * width) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }
}