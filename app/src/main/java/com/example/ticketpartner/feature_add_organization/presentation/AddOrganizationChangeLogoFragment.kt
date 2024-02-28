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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.IMAGE_EXTENSION
import com.example.ticketpartner.common.MIME_IMAGE_TYPE
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentAddOrganizationChangeLogoBinding
import com.example.ticketpartner.utils.Utility.getFile
import java.io.File

class AddOrganizationChangeLogoFragment : Fragment() {
    private lateinit var binding: FragmentAddOrganizationChangeLogoBinding
    private val viewModel: AddOrganizationViewModel by activityViewModels()
    private val requestCodeCameraPermission = 1001
    private var selectedFile: File? = null
    private var selectedFileUri: String = ""
    private var mimeType: String? = null
    private var organizationName: String = ""
    private var countryId: String = ""
    private var countryN: String = ""
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

        arguments?.let {
           // countryId = it.getString("countryId", "")
            countryN = it.getString("countryName","")
        }

        binding.etCountry.setText(countryN)
    }

    private fun initView() {



        /** get organization name from user */
        binding.etOrganizationName.doAfterTextChanged {
            organizationName = it.toString().trim()
        }

        /** get country id from selected country name */
        binding.etCountry.doAfterTextChanged {
           findNavController().navigate(R.id.AddOrgCountrySearchFragment)
        }

        /** Redirect on add organization social link page */
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.addOrganizationSocialFragment)
        }

        /** ask permission and launch gallery on button click */
        binding.llChangeLogoBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) askForGalleryPermission() else launchImagePicker()
        }

        /***/
        binding.btnNext.setOnClickListener {
            if (checkValidation()) {
                Toast.makeText(requireContext(), "validate", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }
            /*      selectedFile?.let { file ->
                      viewModel.addOrganization(file, "raj", "8")
                  }*/
        }
    }

    private fun checkValidation(): Boolean {
        var isValid = true
        if (organizationName.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.please_enter_organization_name)
            )
            return false
        }
        if (countryId.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.please_select_country)
            )
            return false
        }
        if (selectedFileUri.isEmpty()) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                getString(R.string.select_change_logo_image)
            )
            return false
        }
        return isValid
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
                        setSelectedFile(it, IMAGE_EXTENSION)
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
        selectedFileUri = getFile(requireContext(), uri, fileExtension).toString()
        mimeType = MIME_IMAGE_TYPE
    }


    /** set image into imageView by using Glide */
    private fun handleImageResult(data: Intent?) {
        val image = data?.data
        if (image != null) {
            Glide.with(this)
                .load(image)
                .into(binding.ivImage)
        }
    }

    /** ask gallery access permission */
    private fun askForGalleryPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(CAMERA, READ_EXTERNAL_STORAGE),
            requestCodeCameraPermission
        )
    }

}