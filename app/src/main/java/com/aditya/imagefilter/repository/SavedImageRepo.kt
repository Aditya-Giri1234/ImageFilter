package com.aditya.imagefilter.repository

import android.graphics.Bitmap
import java.io.File

interface SavedImageRepo {
    suspend fun loadSavedImage():List<Pair<File, Bitmap>>?
}