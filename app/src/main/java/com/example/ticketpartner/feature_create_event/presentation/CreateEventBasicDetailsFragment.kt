package com.example.ticketpartner.feature_create_event.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
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
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesResponse
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesUIState
import com.example.ticketpartner.feature_create_event.presentation.adapter.AddImagesMediaAdapter
import com.example.ticketpartner.feature_create_event.presentation.adapter.AddMoreImagesAdapter
import com.example.ticketpartner.feature_create_event.presentation.adapter.CreateEventTypesAdapter
import com.example.ticketpartner.feature_create_event.presentation.adapter.TimeZoneSpinnerAdapter
import com.example.ticketpartner.utils.CameraUtils
import com.example.ticketpartner.utils.DatePickerUtility
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.TimePickerUtility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class CreateEventBasicDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventBasicDetailsBinding
    private val viewModel: CreateEventViewModel by activityViewModels()
    private val currentDateTime = Calendar.getInstance()
    private val requestCodeCameraPermission = 1002
    private var selectedButtonId: Int = -1
    private lateinit var addMoreImageAdapter: AddMoreImagesAdapter
    private lateinit var addImageMediaAdapter: AddImagesMediaAdapter

    @Inject
    lateinit var logUtil: LogUtil
    private var selectedFileUri: String = ""

    private var timeZoneListResponse: ArrayList<CreateEventGetTimeZoneResponse> = ArrayList()
    private var timeZoneNameListResponse: ArrayList<String> = ArrayList()

    private var eventTypesListResponse: ArrayList<CreateEventTypesResponse> = ArrayList()
    private var eventTypesNameListResponse: ArrayList<String> = ArrayList()

    private var selectedCoverImageFile: File? = null
    private var addMoreImagesListFile: ArrayList<File> = ArrayList()
    private var addImagesMediaListFile: ArrayList<File> = ArrayList()

    private var addMoreImagesBitmapList: ArrayList<Bitmap> = ArrayList()
    private var addImagesMediaBitmapList: ArrayList<Bitmap> = ArrayList()

    private val tvListForDateTime : ArrayList<AppCompatTextView> = ArrayList()

    private var eventStartDate = ""
    private var eventEndDate = ""
    private var eventStartTime = ""
    private var eventEndTime = ""

    private var startDateDoorOpen = ""
    private var endDateDoorOpen = ""
    private var startTimeDoorOpen = ""
    private var endTimeDoorOpen = ""


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
    }

    private fun makeGetApisCall() {
        viewModel.getTimeZone()
        viewModel.getEventType()
    }

    private fun observeResponse() {
        viewModel.getEventTypeResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventTypesUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventTypesUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    for (i in 0 until it.result.data?.size!!) {
                        eventTypesListResponse.add(it.result)
                        eventTypesNameListResponse.add(it.result.data[i]?.event_type_title.toString())
                    }
                    setEventTypesAdapter()
                }

                is CreateEventTypesUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }

        /** observe time zone response */
        viewModel.getTimeZoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventGetTimeZoneUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventGetTimeZoneUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    for (i in 0 until it.result.data?.size!!) {
                        timeZoneListResponse.add(it.result)
                        timeZoneNameListResponse.add(it.result.data[i]?.time_zone_name.toString())
                    }
                    setAutoCompleteDropDown()
                }

                is CreateEventGetTimeZoneUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setEventTypesAdapter() {
        binding.spinnerEvenTypes.hint = getString(R.string.select_event_type)
        val adapter =
            CreateEventTypesAdapter(
                requireContext(),
                R.layout.custom_drop_down_layout,
                eventTypesNameListResponse
            )
        binding.spinnerEvenTypes.setAdapter(adapter)
        binding.spinnerEvenTypes.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            val selectedItemId = eventTypesListResponse[0].data?.get(position)?.id

            Toast.makeText(
                requireContext(),
                "Selected Item ID: $selectedItemId",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setAutoCompleteDropDown() {
        binding.spinnerTimezone.hint = getString(R.string.select_time_zone)
        val adapter =
            TimeZoneSpinnerAdapter(
                requireContext(),
                R.layout.custom_drop_down_layout,
                timeZoneNameListResponse
            )
        binding.spinnerTimezone.setAdapter(adapter)
        binding.spinnerTimezone.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            val selectedItemId = timeZoneListResponse[0].data?.get(position)?.id

            Toast.makeText(
                requireContext(),
                "Selected Item ID: $selectedItemId",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun initView() {
        setAddMoreImagesAdapter(addMoreImagesBitmapList)
        setAddImagesMediaAdapter(addImagesMediaBitmapList)

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

        binding.switchMediaFromPast.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.clAddImageMediaLayout.visibility =
                View.VISIBLE else binding.clAddImageMediaLayout.visibility = View.GONE
        }

        viewDateTime.startDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.startDate.tvDate,::getSelectedStartDate)
         ///   val startDateText = viewDateTime.startDate.tvDate.text.toString()
           /* startDate = startDateText
            startDateDoorOpen  = startDateText

            viewDoorOpen.startDateDoorOpen.tvDate.text = startDateText
            viewDoorOpen.endDateDoorOpen.tvDate.text =  startDateText*/
        }

        viewDateTime.startTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(),::getSelectedStartTime)
            //startTime = viewDateTime.startTime.tvTime.text.toString()
        }

        viewDateTime.endDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.endDate.tvDate,::getSelectedEndDate)
        }

        viewDateTime.endTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(),::getSelectedEndTime)
        }

        /** for door open's start-end date, start-end time */
     /*   viewDoorOpen.startDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(
                requireContext(),
                viewDoorOpen.startDateDoorOpen.tvDate
            )
        }*/

        viewDoorOpen.startTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(
                requireContext(),::getOpenDoorStartTime)
        }

      /*  viewDoorOpen.endDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDoorOpen.endDateDoorOpen.tvDate)
        }*/

        viewDoorOpen.endTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(),::getOpenDoorEndTime)
        }

        binding.rlPickCoverImage.setOnClickListener {
            selectedButtonId = R.id.rlPickCoverImage
            checkIsPermissionGranted()
        }

        binding.ivDelete.setOnClickListener {
            binding.rlPickCoverImage.visibility = View.VISIBLE
            binding.rlCoverImage.visibility = View.GONE
        }

        binding.rlAddMoreImage.setOnClickListener {
            selectedButtonId = R.id.rlAddMoreImage
            checkIsPermissionGranted()
        }

        binding.clAddImageMediaLayout.setOnClickListener {
            selectedButtonId = R.id.clAddImageMediaLayout
            checkIsPermissionGranted()
        }
    }

    private fun getOpenDoorEndTime(openDoorEndTime: String) {

    }

    private fun getOpenDoorStartTime(openDoorStartTime: String) {

    }

    private fun getSelectedEndTime(endTime: String) {

    }

    private fun getSelectedStartTime(startTime: String) {
        val viewDateTime = binding.includeDateTime
        viewDateTime.startTime.tvTime.text = startTime

        val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDateTime.time)
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentDateTime.time)

        Log.e("TAG", "getSelectedStartTime: $currentTime", )
        Log.e("TAG", "getSelectedStartDate: $currentDate", )

        if (eventStartDate <= currentDate.toString() && startTime<currentTime.toString()){
            SnackBarUtil.showErrorSnackBar(binding.root, "you can't select before $currentTime")
        }

   /*     val currentTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = currentTimeFormat.format(currentDateTime.time)*/
    }

    private fun getSelectedEndDate(endDate: String) {

    }

    private fun getSelectedStartDate(startDate: String) {
        val viewDateTime = binding.includeDateTime
        viewDateTime.startDate.tvDate.text = startDate
        eventStartDate = startDate
        val viewDoorOpen = binding.includeDateTime
        viewDoorOpen.startDateDoorOpen.tvDate.text = startDate
        viewDoorOpen.endDateDoorOpen.tvDate.text = startDate
    }


    private fun setTabViewAdapter() {
        val titleList = resources.getStringArray(R.array.tab_create_event)
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

    /** use this funcation to open image/camera dialog
     * it will check permission then open dialog */
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

    /** open bottom sheet dialog for
     * chose image resource camera/gallery */
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
        dialog.setContentView(dialogView.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /** launch gallery with this function */
    private fun launchImagePicker() {
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = PICK_IMAGE_INTENT_TYPE
        getImageLauncher.launch(pickImageIntent)
    }

    /** capture image from camera */
    private fun captureImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureImageLauncher.launch(takePictureIntent)
    }

    /** get data from gallery based on button click id */
    private val getImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            when (selectedButtonId) {
                R.id.rlPickCoverImage -> {
                    try {
                        val data = result.data?.data
                        data?.let {
                            val bitImage = CameraUtils.uriToBitmap(requireContext(), it)
                            setCoverImage(bitImage)
                        }
                        selectedCoverImageFile = getFileFromUri(data)
                    } catch (e: Exception) {
                    }
                }

                R.id.rlAddMoreImage -> {
                    try {
                        val data = result.data?.data
                        val bitImage = data?.let { CameraUtils.uriToBitmap(requireContext(), it) }
                        bitImage?.let { addMoreImagesBitmapList.add(it) }
                        setAddMoreImagesAdapter(addMoreImagesBitmapList)
                    } catch (e: Exception) {
                    }
                }

                R.id.clAddImageMediaLayout -> {
                    try {
                        val data = result.data?.data
                        val bitImage = data?.let { CameraUtils.uriToBitmap(requireContext(), it) }
                        bitImage?.let { addImagesMediaBitmapList.add(it) }
                        setAddImagesMediaAdapter(addImagesMediaBitmapList)
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }


    private fun setCoverImage(data: Bitmap?) {
        binding.rlPickCoverImage.visibility = View.GONE
        binding.rlCoverImage.visibility = View.VISIBLE
        binding.ivCoverImage.setImageBitmap(data)
    }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                when (selectedButtonId) {
                    R.id.rlPickCoverImage -> {
                        try {
                            val data = result.data!!.extras?.get("data") as Bitmap
                            setCoverImage(data)
                            selectedCoverImageFile =
                                CameraUtils.saveBitmapAsFileWithMaxSizeInMB(data, 10)
                        } catch (e: Exception) {
                        }
                    }

                    R.id.rlAddMoreImage -> {
                        val data = result.data!!.extras?.get("data") as Bitmap
                        data?.let { addMoreImagesBitmapList.add(it) }
                        setAddMoreImagesAdapter(addMoreImagesBitmapList)
                    }

                    R.id.clAddImageMediaLayout -> {
                        val data = result.data!!.extras?.get("data") as Bitmap
                        data?.let { addImagesMediaBitmapList.add(it) }
                        setAddImagesMediaAdapter(addImagesMediaBitmapList)
                    }
                }
            }
        }


    /** this function will convert
     *  bitmap into file with max 10 MB size */
    private fun getFileFromUri(data: Uri?): File? {
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

    private fun setAddMoreImagesAdapter(addMoreImagesUriList: ArrayList<Bitmap>) {
        addMoreImageAdapter = AddMoreImagesAdapter(
            requireContext(),
            addMoreImagesUriList,
            ::onDeleteClickAddMoreImages
        )
        binding.rvAddMoreImages.adapter = addMoreImageAdapter
        addMoreImageAdapter.notifyDataSetChanged()
    }

    private fun setAddImagesMediaAdapter(addImagesMediaUriList: ArrayList<Bitmap>) {
        addImageMediaAdapter = AddImagesMediaAdapter(
            requireContext(),
            addImagesMediaUriList,
            ::onDeleteClickAddImagesMedia
        )
        binding.rvAddImagesMedia.adapter = addImageMediaAdapter
        addImageMediaAdapter.notifyDataSetChanged()
    }

    private fun onDeleteClickAddMoreImages(position: Int) {
        addMoreImagesBitmapList.removeAt(position)
        setAddMoreImagesAdapter(addMoreImagesBitmapList)
    }

    private fun onDeleteClickAddImagesMedia(position: Int) {
        addImagesMediaBitmapList.removeAt(position)
        setAddImagesMediaAdapter(addImagesMediaBitmapList)
    }
}