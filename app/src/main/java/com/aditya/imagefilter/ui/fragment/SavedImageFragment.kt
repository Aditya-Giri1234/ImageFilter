package com.aditya.imagefilter.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aditya.imagefilter.R
import com.aditya.imagefilter.adapter.ImageFilterAdapter
import com.aditya.imagefilter.adapter.SavedImageAdapter
import com.aditya.imagefilter.databinding.FragmentSavedImageBinding
import com.aditya.imagefilter.model.Resource
import com.aditya.imagefilter.repository.SavedImageListener
import com.aditya.imagefilter.util.Constant
import com.aditya.imagefilter.util.Helper
import com.aditya.imagefilter.viewModel.SavedImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class SavedImageFragment : Fragment()  , SavedImageListener{


    private var _binding: FragmentSavedImageBinding? = null
    private val binding: FragmentSavedImageBinding get() = _binding!!

    private val viewModel: SavedImageViewModel by viewModels()

    @Inject
    lateinit var savedImageAdapter: SavedImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedImageBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObserver()
        initUI()
    }

    private fun subscribeToObserver() {
        viewModel.savedImage.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        savedImageAdapter.submitList(it)
                    } ?: run {
                        binding.rvSavedImages.isGone = true
                        binding.linearNoImageFound.isGone=false
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Helper.customToast(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }

        }
    }

    private fun initUI() {
        binding.apply {
            toolbar.toolbarTitle.text=Constant.SAVED_IMAGES
            rvSavedImages.apply {
                adapter=savedImageAdapter
            }
        }
        viewModel.getAllSavedImages()

        setListeners()
    }

    private fun setListeners() {
        binding.run {
            toolbar.backHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun showProgressBar() {
        binding.pbSavedImage.isGone = false
        binding.rvSavedImages.isGone = true
    }

    private fun hideProgressBar() {
        binding.pbSavedImage.isGone = true
        binding.rvSavedImages.isGone = false
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onImageClicked(file: File) {

        val fileUri=FileProvider.getUriForFile(requireContext() ,"${requireContext().packageName}.provider" ,file)

        val action:NavDirections=SavedImageFragmentDirections.actionSavedImageFragmentToFilterImageFragment(fileUri)

        findNavController().navigate(
            action ,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.animator.fade_in
                    exit = android.R.animator.fade_out
                }
            }
        )

    }


}