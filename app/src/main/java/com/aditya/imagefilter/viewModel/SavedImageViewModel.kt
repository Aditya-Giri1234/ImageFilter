package com.aditya.imagefilter.viewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.imagefilter.model.Resource
import com.aditya.imagefilter.repositoryImpl.SavedImageRepoImp
import com.aditya.imagefilter.util.MyLogger
import dagger.hilt.EntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class SavedImageViewModel @Inject constructor( val app:Application) : AndroidViewModel(app) {

    @Inject
    lateinit var savedImageRepoImp:SavedImageRepoImp

    //region:: Get all saved image

    private val _savedImages=MutableLiveData<Resource<List<Pair<File,Bitmap>>?>>()

    val savedImage:LiveData<Resource<List<Pair<File,Bitmap>>?>> get() = _savedImages

    fun getAllSavedImages()=viewModelScope.launch {
        runCatching {
            MyLogger.d(msg = "SGetting all filter images in progress ....")
            _savedImages.postValue(Resource.Loading())
            savedImageRepoImp.loadSavedImage()
        }.onSuccess {
            MyLogger.i(msg = "Getting all filter images is successfully!")
            MyLogger.v(msg = it , isJson = true)
            _savedImages.postValue(Resource.Success(it))
        }.onFailure {
            MyLogger.e(msg = "Some error occurred during fetch filter images :-> ${it.message}")
            _savedImages.postValue(Resource.Error(it.message))
        }
    }

    //endregion

}