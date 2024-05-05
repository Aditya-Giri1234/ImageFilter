package com.aditya.imagefilter.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aditya.imagefilter.R
import com.aditya.imagefilter.databinding.FragmentFilterImageBinding
import com.aditya.imagefilter.util.MyLogger


class FilterImageFragment : Fragment() {

    private var _binding: FragmentFilterImageBinding? = null
    private val binding: FragmentFilterImageBinding get() = _binding!!

    private val args: FilterImageFragmentArgs by navArgs()
    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFilterImageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.apply {
            fileUri = args.imagePath
            ivFilterImage.setImageURI(args.imagePath)
            backHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        setListeners()
    }

    private fun setListeners() {
        binding.run {
            fabShare.setOnClickListener {
                MyLogger.i(msg = "Image now send with file path is :-> $fileUri")
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    type = "image/*"
                }.also { startActivity(it) }
            }
        }
    }

}