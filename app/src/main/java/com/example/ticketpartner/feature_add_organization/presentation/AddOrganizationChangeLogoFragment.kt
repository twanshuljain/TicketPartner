package com.example.ticketpartner.feature_add_organization.presentation

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ticketpartner.R
import com.example.ticketpartner.common.IMAGE_EXTENSION
import com.example.ticketpartner.common.MIME_IMAGE_TYPE
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentAddOrganizationChangeLogoBinding
import com.example.ticketpartner.utils.Utility.getFile
import java.io.File

class AddOrganizationChangeLogoFragment : Fragment() {
    private lateinit var binding: FragmentAddOrganizationChangeLogoBinding
    private val viewModel: AddOrganizationViewModel by activityViewModels()
    private val requestCodeCameraPermission = 1001
    private var selectedFile: File? = null
    private var mimeType: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddOrganizationChangeLogoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.addOrganizationSocialFragment)
        }

        binding.llChangeLogoBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) askForGalleryPermission() else launchImagePicker()
        }

        binding.btnNext.setOnClickListener {
            selectedFile?.let {file ->
                viewModel.addOrganization(file,"raj","8")
            }
        }
    }

    private fun launchImagePicker() {
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"
        pickImageLauncher.launch(pickImageIntent)
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    it.data?.let {
                        setSelectedFile(it,IMAGE_EXTENSION)
                    }
                }
                val data: Intent? = result.data

                handleImageResult(data)

            } else {
                SnackBarUtil.showErrorSnackBar(binding.root, "Image picking cancelled")
            }
        }

    private fun setSelectedFile(uri: Uri, fileExtension: String) {
        selectedFile = getFile(requireContext(), uri, fileExtension)
            mimeType = MIME_IMAGE_TYPE
    }


    private fun handleImageResult(data: Intent?) {
        val image = data?.data
        if (image != null) {
            Glide.with(this)
                .load(image)
                .into(binding.ivImage)
        }
    }

    private fun askForGalleryPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(CAMERA, READ_EXTERNAL_STORAGE),
            requestCodeCameraPermission
        )
    }

}