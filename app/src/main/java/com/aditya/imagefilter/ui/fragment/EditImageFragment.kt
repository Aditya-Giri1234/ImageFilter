package com.aditya.imagefilter.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditya.imagefilter.adapter.ImageFilterAdapter
import com.aditya.imagefilter.databinding.EditImageActivityBinding
import com.aditya.imagefilter.model.ImageFilter
import com.aditya.imagefilter.model.Resource
import com.aditya.imagefilter.repository.ImageFilterListener
import com.aditya.imagefilter.util.Constant
import com.aditya.imagefilter.util.Helper
import com.aditya.imagefilter.util.MyLogger
import com.aditya.imagefilter.viewModel.EditImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import javax.inject.Inject


@AndroidEntryPoint
class EditImageFragment: Fragment() , ImageFilterListener {


    private lateinit var binding: EditImageActivityBinding

    private val arg: EditImageFragmentArgs by navArgs()
    private val editImageViewModel: EditImageViewModel by viewModels()

    @Inject
    lateinit var imageFilterAdapter: ImageFilterAdapter


    private val gpuImage:GPUImage by lazy {
        GPUImage(requireContext())
    }

    //Image Bitmap
    private lateinit var originalBitmap:Bitmap



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MyLogger.d(isFunctionCall = true)
        binding = EditImageActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserver()
        intiUi()
    }

    private fun subscribeToObserver() {
        editImageViewModel.imagePreview.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {

                        MyLogger.i(msg = "User selected image now showing!")

                        originalBitmap=it
                        editImageViewModel.setFilterImage(it)

                        with(originalBitmap){
                            gpuImage.setImage(this)
                            binding.ivImagePreview.setImageBitmap(this)
                            editImageViewModel.loadImageFilters(this)
                        }

                    } ?: run {
                        MyLogger.e(msg = "User not selected any pic!")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                else -> {
                    hideProgressBar()
                    Helper.customToast(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
        editImageViewModel.imageFilterListData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        imageFilterAdapter.submitList(it as ArrayList<ImageFilter>)
                    } ?: run {

                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                else -> {
                    hideProgressBar()
                    Helper.customToast(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
        editImageViewModel.filterBitmap.observe(viewLifecycleOwner){
            binding.ivImagePreview.setImageBitmap(it)
        }
        editImageViewModel.saveFilterImage.observe(viewLifecycleOwner){response->
            when (response) {
                is Resource.Success -> {
                    hideToolbarProgressBar()
                    response.data?.let {
                        MyLogger.i(msg = "File save successful than navigate to filter image fragment !")
                        val action:NavDirections=EditImageFragmentDirections.actionEditImageFragmentToFilterImageFragment(it)
                        findNavController().navigate(action)

                        editImageViewModel.resetSaveFilterImage()


                    } ?: run {

                    }
                }

                is Resource.Loading -> {
                    showToolbarProgressBar()
                }

                else -> {
                    hideToolbarProgressBar()
                    Helper.customToast(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }


    private fun intiUi() {
        binding.run {
            getImage()
            myToolbar.done.isGone=false
            rvFilter.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = imageFilterAdapter
            }
        }

        setListener()
    }

    private fun setListener() {
        binding.run {
            myToolbar.backHome.setOnClickListener {
                findNavController().popBackStack()
            }

            myToolbar.done.setOnClickListener {
                editImageViewModel.filterBitmap.value?.let {
                    MyLogger.d(msg = "File saving process is start now !")
                    editImageViewModel.saveFilterImage(it)
                }
            }

            /*
            Whenever user long click on image then original image show not filter image but when click on
            image it show filter image , so that user can see differences.
             */

            ivImagePreview.setOnLongClickListener {
                ivImagePreview.setImageBitmap(originalBitmap)
                return@setOnLongClickListener true
            }

            ivImagePreview.setOnClickListener {
                ivImagePreview.setImageBitmap(editImageViewModel.filterBitmap.value)
            }

        }

    }


    private fun getImage() {
        binding.run {
            //get Data
            arg.uriArgument.let {
                ivImagePreview.isGone = false
                editImageViewModel.getImagePreview(it)
            }
        }
    }

    private fun showToolbarProgressBar(){
        binding.apply {
            myToolbar.done.isGone=true
            myToolbar.savingProgress.isGone=false
        }
    }

    private fun hideToolbarProgressBar(){
        binding.apply {
            myToolbar.done.isGone=false
            myToolbar.savingProgress.isGone=true
        }
    }

    private fun showProgressBar() {
        binding.progressCircular.isGone = false
        binding.pbImageFiler.isGone = false
    }

    private fun hideProgressBar() {
        binding.progressCircular.isGone = true
        binding.pbImageFiler.isGone = true
    }

    private fun displayImage(uri: Uri) {
        binding.ivImagePreview.setImageBitmap(
            BitmapFactory.decodeStream(
                requireContext().contentResolver.openInputStream(
                    uri
                )
            )
        )
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {

        MyLogger.d(isFunctionCall = true)
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                editImageViewModel.setFilterImage(bitmapWithFilterApplied)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MyLogger.d(isFunctionCall = true)
    }
}