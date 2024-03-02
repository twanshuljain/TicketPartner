package com.example.ticketpartner.feature_add_organization.presentation

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ticketpartner.R
import com.example.ticketpartner.common.IMAGE_EXTENSION
import com.example.ticketpartner.common.MIME_IMAGE_TYPE
import com.example.ticketpartner.common.ORGANIZATION_ID
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentAddOrganizationChangeLogoBinding
import com.example.ticketpartner.databinding.LayoutBottomSheetImagePickerBinding
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.Utility.getFile
import com.google.android.material.bottomsheet.BottomSheetDialog
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
    private var countryName: String = ""
    private var imageUri: String = ""

    private var imageUriS: Uri? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val permissions = arrayOf(
        CAMERA,
        WRITE_EXTERNAL_STORAGE
    )


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

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    openImagePickerBottomSheet()
                } else {
                    askForGalleryPermission()
                }
            }
    }

    private fun observeResponseData() {
        viewModel.getAddOrganization.observe(viewLifecycleOwner) {
            when (it) {
                is AddOrganizationUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is AddOrganizationUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                    val bundle = Bundle()
                    bundle.putString(ORGANIZATION_ID, it.result.data?.id.toString())
                    findNavController().navigate(R.id.addOrganizationSocialFragment, bundle)
                }

                is AddOrganizationUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun initView() {
        handleImageResult()

        viewModel.getCountyName.observe(viewLifecycleOwner) {
            countryName = it
            binding.tvSearchCountryBtn.setText(it)
        }

        /** get country id from selected country name */
        viewModel.getCountyId.observe(viewLifecycleOwner) {
            countryId = it
        }

        binding.titleName.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        /** get organization name from user */
        binding.etOrganizationName.doAfterTextChanged {
            organizationName = it.toString().trim()
        }

        binding.tvSearchCountryBtn.setOnClickListener {
            findNavController().navigate(R.id.addOrgCountrySearchFragment)
        }

        /** Redirect on add organization social link page */
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.addOrganizationSocialFragment)
        }

        /** ask permission and launch gallery on button click */
        binding.llChangeLogoBtn.setOnClickListener {
            checkIsPermissionGranted()

        }

        /***/
        binding.btnNext.setOnClickListener {
            if (checkValidation()) {
                selectedFile?.let { file ->
                    viewModel.addOrganization(file, organizationName, countryId)
                    observeResponseData()
                }
            }
        }
    }


    private fun openImagePickerBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutBottomSheetImagePickerBinding.inflate(layoutInflater)
        dialogView.btnGallery.setOnClickListener {
            launchImagePicker()
            dialog.dismiss()
        }
        dialogView.btnCamera.setOnClickListener {
            captureImage()
            dialog.dismiss()
        }
        dialogView.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }


    private fun checkIsPermissionGranted() {
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(requireContext(), permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(permission)
            } else {
                openImagePickerBottomSheet()
            }
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

    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        pickImageLauncher.launch(cameraIntent)
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
                imageUri = data?.data!!.toString()
                handleImageResult()
            }
        }

    private fun setSelectedFile(uri: Uri, fileExtension: String) {
        selectedFile = getFile(requireContext(), uri, fileExtension)
        selectedFileUri = getFile(requireContext(), uri, fileExtension).toString()
        mimeType = MIME_IMAGE_TYPE
    }


    /** set image into imageView by using Glide */
    private fun handleImageResult() {
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.updateSearchCountryName("")
        viewModel.updateSearchCountryId("")
    }

}