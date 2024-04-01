package com.example.ticketpartner.feature_create_event.presentation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.common.PICK_IMAGE_INTENT_TYPE
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.TEN
import com.example.ticketpartner.common.ZERO
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
import com.example.ticketpartner.utils.Utility.areTimesInOrder
import com.example.ticketpartner.utils.Utility.compareDates
import com.example.ticketpartner.utils.Utility.compareTimes
import com.example.ticketpartner.utils.Utility.isTimeInRange
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

    private val tvListForDateTime: ArrayList<AppCompatTextView> = ArrayList()

    private var eventStartDate = ""
    private var eventEndDate = ""
    private var eventStartTime = ""
    private var eventEndTime = ""

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

    @RequiresApi(Build.VERSION_CODES.O)
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        logUtil = LogUtil()
        setAddMoreImagesAdapter(addMoreImagesBitmapList)
        setAddImagesMediaAdapter(addImagesMediaBitmapList)

        val viewDateTime = binding.includeDateTime
        val viewDoorOpen = binding.includeDateTime

        viewDateTime.startDate.tvName.text = "Start Date"
        viewDateTime.endDate.tvName.text = "End Date"

        viewDateTime.startTime.tvName.text = "Start Time"
        viewDateTime.endTime.tvName.text = "End Time"

        viewDateTime.startTimeDoorOpen.tvName.text = "Start Time"
        viewDateTime.endTimeDoorOpen.tvName.text = "End Time"

        binding.switchMediaFromPast.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.clAddImageMediaLayout.visibility =
                View.VISIBLE else binding.clAddImageMediaLayout.visibility = View.GONE
        }

        viewDateTime.startDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(
                requireContext(),
                ::getSelectedStartDate
            )
            ///   val startDateText = viewDateTime.startDate.tvDate.text.toString()
            /* startDate = startDateText
             startDateDoorOpen  = startDateText

             viewDoorOpen.startDateDoorOpen.tvDate.text = startDateText
             viewDoorOpen.endDateDoorOpen.tvDate.text =  startDateText*/
        }

        viewDateTime.startTime.timeLayout.setOnClickListener {
            if (isStartDateNotEmpty(eventStartDate))
                TimePickerUtility.getSelectedTime(requireContext(), ::getSelectedStartTime)
        }

        viewDateTime.endDate.dateLayout.setOnClickListener {
            if (isStartDateNotEmpty(eventStartDate))
                DatePickerUtility.getSelectedDate(
                    requireContext(),
                    ::getSelectedEndDate
                )
        }

        viewDateTime.endTime.timeLayout.setOnClickListener {
            if (isStartDateNotEmpty(eventStartDate))
                TimePickerUtility.getSelectedTime(requireContext(), ::getSelectedEndTime)
        }

        /** for door open's start-end date, start-end time */
        /*   viewDoorOpen.startDateDoorOpen.dateLayout.setOnClickListener {
               DatePickerUtility.getSelectedDate(
                   requireContext(),
                   viewDoorOpen.startDateDoorOpen.tvDate
               )
           }*/

        viewDoorOpen.startTimeDoorOpen.timeLayout.setOnClickListener {
            if (isStartDateNotEmpty(eventStartDate))
                TimePickerUtility.getSelectedTime(
                    requireContext(), ::getOpenDoorStartTime
                )
        }

        /*  viewDoorOpen.endDateDoorOpen.dateLayout.setOnClickListener {
              DatePickerUtility.getSelectedDate(requireContext(), viewDoorOpen.endDateDoorOpen.tvDate)
          }*/

        viewDoorOpen.endTimeDoorOpen.timeLayout.setOnClickListener {
            if (isStartDateNotEmpty(eventStartDate))
                TimePickerUtility.getSelectedTime(requireContext(), ::getOpenDoorEndTime)
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

        binding.titleBar.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun isStartDateNotEmpty(eventStartDate: String): Boolean {
        return if (eventStartDate.isNullOrEmpty()) {
            SnackBarUtil.showErrorSnackBar(binding.root, "Please select start date first")
            false
        } else {
            true
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOpenDoorStartTime(openDoorStartTime: String) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        val givenDateString = eventStartDate // given date string
        val givenDate = LocalDate.parse(givenDateString, dateFormatter)

        val firstTimeString = eventStartTime // first time string

        val firstTime = givenDate.atTime(LocalTime.parse(firstTimeString, timeFormatter))
        val secondTime = givenDate.atTime(LocalTime.parse(openDoorStartTime, timeFormatter))

        if (areTimesInOrder(firstTime, secondTime)) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                "time is bigger than event start time"
            )
        } else {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                "before given time"
            )
            startTimeDoorOpen = openDoorStartTime
            binding.includeDateTime.startTimeDoorOpen.tvTime.text = openDoorStartTime
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOpenDoorEndTime(openDoorEndTime: String) {

        val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        val givenDateString = eventStartDate// Example given date string
        val givenDate = LocalDate.parse(givenDateString, dateFormatter)

        val firstTimeString = eventStartTime// Example first time string
        val secondTimeString = startTimeDoorOpen// Example second time string

        val firstTime = givenDate.atTime(LocalTime.parse(firstTimeString, timeFormatter))
        val secondTime = givenDate.atTime(LocalTime.parse(secondTimeString, timeFormatter))
        val selectedTime = givenDate.atTime(LocalTime.parse(openDoorEndTime, timeFormatter))

        if (isTimeInRange(selectedTime, firstTime, secondTime)) {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                "The selected time is between the given first time and second time for the given date."
            )
            binding.includeDateTime.endTimeDoorOpen.tvTime.text = openDoorEndTime
            endTimeDoorOpen = openDoorEndTime
        } else {
            SnackBarUtil.showErrorSnackBar(
                binding.root,
                "Time should be after open door and before event start time."
            )
        }
    }

    private fun getSelectedEndTime(endTime: String) {
        if (eventStartDate == eventEndDate) {
            val comparisonResult = compareTimes(eventStartTime, endTime)
            when {
                comparisonResult < ZERO -> {
                    binding.includeDateTime.endTime.tvTime.text = endTime
                    eventEndTime = endTime
                }

                comparisonResult > ZERO -> SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    "End time can't be before start time."
                )
            }
        } else {
            binding.includeDateTime.endTime.tvTime.text = endTime
            eventEndTime = endTime
        }
    }

    private fun getSelectedStartTime(startTime: String) {
        val viewDateTime = binding.includeDateTime

        val currentDate =
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDateTime.time)
        val currentTime =
            SimpleDateFormat("hh:mm a", Locale.getDefault()).format(currentDateTime.time)

        val comparisonResult = compareTimes(currentTime.toString(), startTime)
        if (eventStartDate <= currentDate.toString()) {
            when {
                comparisonResult < ZERO -> {
                    viewDateTime.startTime.tvTime.text = startTime
                    eventStartTime = startTime
                }

                comparisonResult > ZERO -> SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    "time can't be before $currentTime "
                )
            }
        } else {
            viewDateTime.startTime.tvTime.text = startTime
            eventStartTime = startTime
        }
    }

    private fun getSelectedEndDate(endDate: String) {
        isEndDateSmallerThanStartDate(eventStartDate, endDate)
    }

    private fun isEndDateSmallerThanStartDate(startDate: String, endDate: String) {
        val comparisonResult = compareDates(startDate, endDate)
        if (isStartDateNotEmpty(eventStartDate)) {
            when {
                comparisonResult <= ZERO -> {
                    binding.includeDateTime.endDate.tvDate.text = endDate
                    eventEndDate = endDate
                }

                comparisonResult > ZERO -> SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    "End date can't be before start date."
                )
            }
        }
    }


    private fun getSelectedStartDate(startDate: String) {
        val viewDateTime = binding.includeDateTime
        viewDateTime.startDate.tvDate.text = startDate
        eventStartDate = startDate
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
        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = PICK_IMAGE_INTENT_TYPE
        getImageLauncher.launch(intent)
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
                        var count = result.data?.clipData?.itemCount
                        val clipData = result?.data?.clipData
                        if (clipData != null) {
                            for (i in 0..count!!) {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                uri?.let {
                                    if (CameraUtils.getImageSize(requireContext(), it) <= TEN) {
                                        val bitImage =
                                            CameraUtils.uriToBitmap(requireContext(), it)
                                        bitImage?.let { addMoreImagesBitmapList.add(it) }
                                        setAddMoreImagesAdapter(addMoreImagesBitmapList)
                                    } else {
                                        SnackBarUtil.showErrorSnackBar(
                                            binding.root,
                                            "Max image size is 10 MB "
                                        )
                                    }
                                }
                            }
                        } else if (result.data?.data != null) {
                            val uri = result.data?.data
                            uri?.let { img ->
                                if (CameraUtils.getImageSize(requireContext(), img) <= TEN) {
                                    val bitImage = CameraUtils.uriToBitmap(requireContext(), img)
                                    bitImage?.let { addMoreImagesBitmapList.add(it) }
                                    setAddMoreImagesAdapter(addMoreImagesBitmapList)
                                } else {
                                    SnackBarUtil.showErrorSnackBar(
                                        binding.root,
                                        "Max image size is 10 MB "
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        logUtil.log("TAG", e.toString())
                    }
                }

                R.id.clAddImageMediaLayout -> {
                    try {
                        var count = result.data?.clipData?.itemCount
                        val clipData = result?.data?.clipData
                        if (clipData != null) {
                            for (i in 0..count!!) {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                uri?.let { img ->
                                    if (CameraUtils.getImageSize(requireContext(), img) <= TEN) {
                                        val bitImage =
                                            CameraUtils.uriToBitmap(requireContext(), img)
                                        bitImage?.let { addImagesMediaBitmapList.add(it) }
                                        setAddImagesMediaAdapter(addImagesMediaBitmapList)
                                    } else {
                                        SnackBarUtil.showErrorSnackBar(
                                            binding.root,
                                            "Max image size is 10 MB "
                                        )
                                    }
                                }
                            }
                        } else if (result.data?.data != null) {
                            val uri = result.data?.data
                            uri?.let { img ->
                                if (CameraUtils.getImageSize(requireContext(), img) <= TEN) {
                                    val bitImage = CameraUtils.uriToBitmap(requireContext(), img)
                                    bitImage?.let { addImagesMediaBitmapList.add(it) }
                                    setAddImagesMediaAdapter(addImagesMediaBitmapList)
                                } else {
                                    SnackBarUtil.showErrorSnackBar(
                                        binding.root,
                                        "Max image size is 10 MB "
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        logUtil.log("TAG", e.toString())
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