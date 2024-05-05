package com.aditya.imagefilter.repository

import java.io.File

interface SavedImageListener {
    fun onImageClicked(file: File)
}