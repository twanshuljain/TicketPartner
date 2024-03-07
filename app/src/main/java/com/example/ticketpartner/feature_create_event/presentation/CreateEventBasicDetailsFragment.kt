package com.example.ticketpartner.feature_create_event.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.PICK_IMAGE_INTENT_TYPE
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentCreateEventBasicDetailsBinding
import com.example.ticketpartner.databinding.LayoutBottomSheetImagePickerBinding
import com.example.ticketpartner.feature_add_organization.domain.model.ImageModel
import com.example.ticketpartner.feature_add_organization.presentation.adapter.SearchCountryAdapter
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
import com.example.ticketpartner.utils.CameraUtils
import com.example.ticketpartner.utils.DatePickerUtility
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.TimePickerUtility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import javax.inject.Inject

class CreateEventBasicDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventBasicDetailsBinding
    private val viewModel: CreateEventViewModel by activityViewModels()
    private val requestCodeCameraPermission = 1002
    private var selectedButtonId: Int = -1
    private lateinit var addMoreImageAdapter: AddMoreImageAdapter

    @Inject
    lateinit var logUtil: LogUtil
    private var selectedFileUri: String = ""

    private var selectedCoverImageFile: File? = null
    private var addMoreImagesListFile: ArrayList<File> = ArrayList()
    private var addMoreImagesUriList: ArrayList<Uri> = ArrayList()


    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventBasicDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        makeGetApisCall()
        observeResponse()
        setTabViewAdapter()
        checkPermissionCameraStorage()


        /*   //spinner adapter
           ArrayAdapter.createFromResource(
               requireContext(),
               R.array.planets_array,
               android.R.layout.simple_spinner_item
           ).also { adapter ->
               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
               binding.planetsSpinner.adapter = adapter
           }

           val languages = resources.getStringArray(R.array.planets_array)
           binding.planetsSpinner.onItemSelectedListener = object :
               AdapterView.OnItemSelectedListener {
               override fun onItemSelected(
                   parent: AdapterView<*>,
                   view: View, position: Int, id: Long
               ) {
                   Toast.makeText(
                       requireContext(),
                       "selected Item " + " " +
                               "" + languages[position], Toast.LENGTH_SHORT
                   ).show()
               }

               override fun onNothingSelected(parent: AdapterView<*>) {}
           }
   */

        /*
                binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        Toast.makeText(
                            requireContext(),
                            "checked",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "unChecked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }*/
    }

    private fun makeGetApisCall() {
        viewModel.getTimeZone()
    }

    private fun observeResponse() {
        viewModel.getTimeZoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventGetTimeZoneUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventGetTimeZoneUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    //  SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                }

                is CreateEventGetTimeZoneUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun initView() {
        val viewDateTime = binding.includeDateTime
        val viewDoorOpen = binding.includeDateTime

        viewDateTime.startDate.tvName.text = "Start Date"
        viewDateTime.endDate.tvName.text = "End Date"

        viewDateTime.startTime.tvName.text = "Start Time"
        viewDateTime.endTime.tvName.text = "End Time"

        viewDateTime.startDateDoorOpen.tvName.text = "Start Date"
        viewDateTime.endDateDoorOpen.tvName.text = "End Date"

        viewDateTime.startTimeDoorOpen.tvName.text = "Start Time"
        viewDateTime.endTimeDoorOpen.tvName.text = "End Time"

        viewDateTime.startDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.startDate.tvDate)
        }

        viewDateTime.startTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDateTime.startTime.tvTime)
        }

        viewDateTime.endDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.endDate.tvDate)
        }

        viewDateTime.endTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDateTime.endTime.tvTime)
        }

        /** for door open's start-end date, start-end time */
        viewDoorOpen.startDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(
                requireContext(),
                viewDoorOpen.startDateDoorOpen.tvDate
            )
        }

        viewDoorOpen.startTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(
                requireContext(),
                viewDoorOpen.startTimeDoorOpen.tvTime
            )
        }

        viewDoorOpen.endDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDoorOpen.endDateDoorOpen.tvDate)
        }

        viewDoorOpen.endTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDoorOpen.endTimeDoorOpen.tvTime)
        }

        binding.rlPickImage.setOnClickListener {
            checkIsPermissionGranted()
        }

        binding.ivDelete.setOnClickListener {
            binding.rlPickImage.visibility = View.VISIBLE
            binding.rlCoverImage.visibility = View.GONE
        }

        binding.rlAddMoreImage.setOnClickListener {

        }

    }


    private fun setTabViewAdapter() {
        //list of tab title name
        val titleList = resources.getStringArray(R.array.tab_create_event)

        //tabView adapter
        val adapter = TabCreateEvent(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    private fun checkPermissionCameraStorage() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    openImagePickerBottomSheet()
                } else {
                    askForGalleryPermission()
                }
            }
    }

    private fun askForGalleryPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            requestCodeCameraPermission
        )
    }


    //call on button click
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

    private fun openImagePickerBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutBottomSheetImagePickerBinding.inflate(layoutInflater)
        dialogView.btnGallery.setOnClickListener {
            launchImagePicker(R.id.btnGallery)
            dialog.dismiss()
        }
        dialogView.btnCamera.setOnClickListener {
            //  captureImage()
            dialog.dismiss()
        }
        dialogView.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun launchImagePicker(buttonId: Int) {
        selectedButtonId = buttonId
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = PICK_IMAGE_INTENT_TYPE
        getImageLauncher.launch(pickImageIntent)
    }

    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Process the selected image based on the button ID
            when (selectedButtonId) {
                R.id.btnGallery -> {
                    try {
                        val data = result.data?.data
                        selectedFileUri = data.toString()
                        binding.ivCoverImage.setImageURI(data)

                        data?.let {
                            addMoreImagesUriList.add(it)
                        }
                        setAddMoreImagesAdapter()

                        binding.rlPickImage.visibility = View.GONE
                        binding.rlCoverImage.visibility = View.VISIBLE
                        selectedCoverImageFile = getFileFromBitmap(data)

                    } catch (e: Exception) {
                        logUtil.log("TAG", e.message.toString())
                    }
                }
                // Add more cases for other buttons as needed
            }
        }
    }

    /** this function will convert
     *  bitmap into file with max 10 MB size */
    private fun getFileFromBitmap(data: Uri?): File? {
        try {
            data?.let {
                val bitMapImageFile = CameraUtils.uriToBitmap(requireContext(), it)
                bitMapImageFile?.let { bitmap ->
                    return CameraUtils.saveBitmapAsFileWithMaxSizeInMB(bitmap, 10)
                }
            }
        } catch (e: Exception) {
            logUtil.log("TAG", e.message.toString())
        }
        return null
    }

   private fun setAddMoreImagesAdapter() {
        addMoreImageAdapter = AddMoreImageAdapter(requireContext(), addMoreImagesUriList,::onDeleteClickAddMoreImages)
        binding.rvAddMoreImages.adapter = addMoreImageAdapter
        addMoreImageAdapter.notifyDataSetChanged()
    }

    private fun onDeleteClickAddMoreImages(position: Int) {
        addMoreImagesUriList.removeAt(position)
        setAddMoreImagesAdapter()
    }
}