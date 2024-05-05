package com.aditya.imagefilter.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aditya.imagefilter.R
import com.aditya.imagefilter.databinding.ActivitySplashBinding
import com.aditya.imagefilter.util.Constant


class SplashFragment : Fragment() {
    private lateinit var binding: ActivitySplashBinding


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let {
                        // Navigate
                        val action =
                            SplashFragmentDirections.actionSplashFragmentToEditImageFragment(it)
                        findNavController().navigate(
                            action,
                            navOptions { // Use the Kotlin DSL for building NavOptions
                                anim {
                                    enter = android.R.animator.fade_in
                                    exit = android.R.animator.fade_out
                                }
                            }
                        )
                    }

                }

                Activity.RESULT_CANCELED -> {

                }
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.run {
            btnEditImage.setOnClickListener {
                activityResultLauncher.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ).also { pickerIntent ->
                        pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        pickerIntent.type = "image/*"
                    })
            }
            btnSavedImage.setOnClickListener {
                val action: NavDirections =
                    SplashFragmentDirections.actionSplashFragmentToSavedImageFragment()
                findNavController().navigate(
                    action,
                    navOptions { // Use the Kotlin DSL for building NavOptions
                        anim {
                            enter = android.R.animator.fade_in
                            exit = android.R.animator.fade_out
                        }
                    }
                )
            }
        }
    }


}