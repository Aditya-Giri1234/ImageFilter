package com.aditya.imagefilter.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aditya.imagefilter.model.ImageFilter
import com.aditya.imagefilter.model.Resource
import com.aditya.imagefilter.repositoryImpl.EditImageRepoImpl
import com.aditya.imagefilter.util.MyLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditImageViewModel  @Inject constructor( val app:Application) : AndroidViewModel(app) {

    @Inject lateinit var  editImageRepoImpl:EditImageRepoImpl

    //region:: Prepare image preview
    val imagePreview:MutableLiveData<Resource<Bitmap>> = MutableLiveData()

    fun getImagePreview(imageUri:Uri)= viewModelScope.launch {
        imagePreview.postValue(Resource.Loading())

        editImageRepoImpl.prepareImagePreview(imageUri)?.let {
            imagePreview.postValue(Resource.Success(it))
        } ?: run {
            imagePreview.postValue(Resource.Error("Unable to prepare image preview !"))
        }
    }

    //endregion

    //region:: Load image filters
    private val _imageFilterListData:MutableLiveData<Resource<List<ImageFilter>>> = MutableLiveData()
     val imageFilterListData:LiveData<Resource<List<ImageFilter>>> get() = _imageFilterListData

    fun loadImageFilters(originalImage: Bitmap)=viewModelScope.launch {
        runCatching {
            MyLogger.d(msg = "Image filter is loading !")
            _imageFilterListData.postValue(Resource.Loading())
            editImageRepoImpl.getImageFilters(getPreviewImage(originalImage))
        }.onSuccess {imageFilters->
            MyLogger.i(msg = "Loading image filter is successful !")
            _imageFilterListData.postValue(Resource.Success(imageFilters))
        }.onFailure {
            MyLogger.e(msg = "Some error occurred during fetch filters :-> ${it.message}")
            _imageFilterListData.postValue(Resource.Error(it.message))
        }
    }

    private fun getPreviewImage(originalImage:Bitmap):Bitmap{
        return runCatching {
            val previewWidth=150
            val previewHeight=originalImage.height*previewWidth/originalImage.width
            Bitmap.createScaledBitmap(originalImage,previewWidth,previewHeight ,false)
        }.getOrDefault(originalImage)

    }

    //endregion

    //region:: Filter bitmap object
    private val _filterBitmap:MutableLiveData<Bitmap> = MutableLiveData()
    val filterBitmap:LiveData<Bitmap>  get() = _filterBitmap

    fun setFilterImage(bitmap:Bitmap)=viewModelScope.launch {
        _filterBitmap.postValue(bitmap)
    }

    //endregion

    //region:: Save filter image

    private val _saveFilterImage:MutableLiveData<Resource<Uri?>> = MutableLiveData()
    val saveFilterImage:LiveData<Resource<Uri?>>  get() = _saveFilterImage

    fun saveFilterImage(filterBitmap: Bitmap)=viewModelScope.launch {
        runCatching {
            MyLogger.d(msg = "Save  filter image in progress ....")
            _saveFilterImage.postValue(Resource.Loading())
            editImageRepoImpl.saveFilteredImage(filterBitmap)
        }.onSuccess {
            MyLogger.i(msg = "Save  filter image is successful !")
            _saveFilterImage.postValue(Resource.Success(it))
        }.onFailure {
            MyLogger.e(msg = "Some error occurred during save filter image :-> ${it.message}")
            _saveFilterImage.postValue(Resource.Error(it.message))
        }
    }

    fun resetSaveFilterImage(){
        _saveFilterImage.postValue(Resource.Success(null))
    }

    //endregion
}