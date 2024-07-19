package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_DOTS
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.remote.apis.UNAUTHORIZED_USER
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST
import com.example.ticketpartner.databinding.FragmentScanBottomNavQRScanBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.GetCheckInDataLocalDBUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.GetScanReportDataOfflineUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.NameData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrCodeListFromLocalDBUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanResponse
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.example.ticketpartner.feature_scan_module.feature_rfid.NfcViewModel
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.DialogUtils
import com.example.ticketpartner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.example.ticketpartner.utils.TimePickerUtility
import com.example.ticketpartner.utils.Utility
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ScanBottomQRScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavQRScanBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private val nfcViewModel: NfcViewModel by activityViewModels()
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var isTorchOn = false
    private var surfaceWidth: Int = 340
    private var surfaceHeight: Int = 340
    private var currentZoom = 0f
    private lateinit var imageCapture: ImageCapture
    private var getQrCodeListItemLocalDB = ArrayList<DataItems>()
    private var qrCodeListLocalDB = ArrayList<String>()
    private var getCheckInItemListLocalDB = ArrayList<CheckInData>()
    private var getCheckInOrderTicketIdListLocalDB = ArrayList<String>()
    private var isObserved = false
    private var isDialogVisible = false
    private  var mNfcAdapter: NfcAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavQRScanBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCameraPermission()

        /** Observe - network available or not */
        viewModel.isNetworkAvailableObserver.observe(viewLifecycleOwner) {
            if (it) {
                getScannedTicketData()
            } else {
                getScannedTicketDataOffline()
            }
        }
            nfcViewModel.receiveNfcData.observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {
                if (binding.radioBtnRfid.isChecked) {
                    viewModel.isNetworkAvailableObserver.observe(viewLifecycleOwner) { isConnected ->
                        if (!isObserved) {
                            Log.e("TAG", "Received data in activity: $it")
                            handleNetworkConnection(isConnected, it)
                            isObserved = true
                        }
                    }
                }
                nfcViewModel.setNfcValur(EMPTY_STRING)
            }
        }
    }

    private fun getScannedTicketDataOffline() {
        viewModel.getScanReportDataFromLocalDB()
        viewModel.getScanReportDataFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetScanReportDataOfflineUIState.IsLoading -> {}
                is GetScanReportDataOfflineUIState.OnSuccess -> {

                    if (viewModel.totalScanned.toString().isNullOrEmpty()) {
                        val res = it.onSuccess[ZERO]
                        binding.tvTotalScanned.text =
                            getString(R.string.total_scanned) + VERTICAL_DOTS + res?.total_scanned.toString()
                        binding.tvAccepted.text =
                            getString(R.string.accepted) + VERTICAL_DOTS + res?.total_accepted.toString()
                        binding.tvRejected.text =
                            getString(R.string.rejected) + VERTICAL_DOTS + res?.total_rejected.toString()
                    } else {
                        binding.tvTotalScanned.text =
                            getString(R.string.total_scanned) + VERTICAL_DOTS + viewModel.totalScanned.toString()
                        binding.tvAccepted.text =
                            getString(R.string.accepted) + VERTICAL_DOTS + viewModel.totalAccepted.toString()
                        binding.tvRejected.text =
                            getString(R.string.rejected) + VERTICAL_DOTS + viewModel.totalRejected.toString()
                    }
                }

                is GetScanReportDataOfflineUIState.OnFailure -> {}
            }
        }
    }

    private fun initView() {
        getQrCodeListItemLocalDB.clear()
        qrCodeListLocalDB.clear()
        observeQrCodeListFromLocalDB()
        observeCheckInDataFromLocalDB()
        binding.ivZoom.isEnabled = false
        binding.ivTorch.isEnabled = false
        binding.radioBtnQrCode.isChecked = true

        mNfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity())
        viewModel.eventName.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
        }

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner) {
            val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
            subTitle?.visibility = View.VISIBLE
            subTitle?.text = it
        }

        binding.radioBtnQrCode.setOnClickListener {
            binding.apply {
                isQrButtonEnable(true)
            }
        }

        binding.radioBtnRfid.setOnClickListener {
            nfcValidation()
            isQrButtonEnable(false)
        }

        binding.ivTorch.setOnClickListener {
            if (isTorchOn) {
                setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                isTorchOn = false
                binding.ivTorch.setImageResource(R.drawable.ic_torch_off)
            } else {
                setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                isTorchOn = true
                binding.ivTorch.setImageResource(R.drawable.ic_torch_on)
            }
        }

        binding.ivZoom.setOnClickListener {
            if (currentZoom == 0f) {
                currentZoom = 0.5f
                binding.ivZoom.setImageResource(R.drawable.ic_1x_zoom)
                setCameraZoom(cameraSource, 0.5f)
            } else if (currentZoom == 0.5f) {
                currentZoom = 1f
                setCameraZoom(cameraSource, 1f)
                binding.ivZoom.setImageResource(R.drawable.ic_2x_zoom)
            } else if (currentZoom == 1f) {
                currentZoom = 0f
                setCameraZoom(cameraSource, 0f)
                binding.ivZoom.setImageResource(R.drawable.ic_default_zoom_grey)
            }
        }

        binding.btnEndScan.setOnClickListener {
            openScanReportBottomSheet()
        }

    }

    private fun isQrButtonEnable(isQrButtonEnable: Boolean) {
        if (isQrButtonEnable){
            binding.apply {
                radioBtnRfid.isChecked = false
                radioBtnQrCode.isChecked = true
                ivRfidBanner.visibility = View.GONE
                surfaceView.visibility = View.VISIBLE
                ivTorch.visibility = View.VISIBLE
                ivZoom.visibility = View.VISIBLE
                rlRfid.background =
                    requireContext().getDrawable(R.drawable.select_ticket_type_item_light_purple_design)
                rlQrCode.background =
                    requireContext().getDrawable(R.drawable.select_ticket_type_item_purple_design)
            }
        }else{
            binding.apply {
                binding.apply {
                    ivTorch.visibility = View.INVISIBLE
                    ivZoom.visibility = View.INVISIBLE
                    radioBtnQrCode.isChecked = false
                    radioBtnRfid.isChecked = true
                    surfaceView.visibility = View.GONE
                    ivRfidBanner.visibility = View.VISIBLE
                    rlRfid.background =
                        requireContext().getDrawable(R.drawable.select_ticket_type_item_purple_design)
                    rlQrCode.background =
                        requireContext().getDrawable(R.drawable.select_ticket_type_item_light_purple_design)
                }
            }
        }
    }

    private fun nfcValidation() {
        if (mNfcAdapter == null) {
            SnackBarUtil.showCustomSnackBar(binding.root,requireContext().getString(R.string.nfc_not_available))
            return
        } else {
            checkNfcEnabled()
        }
    }

    private fun checkNfcEnabled() {
        if (mNfcAdapter?.isEnabled != true) {
            enableNfcSettingDialog()
        }
    }

    private fun observeCheckInDataFromLocalDB() {
        getCheckInItemListLocalDB.clear()
        getCheckInOrderTicketIdListLocalDB.clear()

        viewModel.getCheckInFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetCheckInDataLocalDBUIState.IsLoading -> {}
                is GetCheckInDataLocalDBUIState.OnSuccess -> {
                    if (!it.onSuccess.isNullOrEmpty()) {
                        for (i in ZERO until it.onSuccess.size) {
                            getCheckInItemListLocalDB.add(it.onSuccess[i])
                            getCheckInOrderTicketIdListLocalDB.add(it.onSuccess[i].order_tickets_id.toString())
                        }
                    }
                }

                is GetCheckInDataLocalDBUIState.OnFailure -> {}
            }
        }
    }

    private fun initCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) askForCameraPermission() else setupControls()
    }


    private fun observeQrCodeListFromLocalDB() {
        viewModel.getQrCodeListFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is QrCodeListFromLocalDBUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrCodeListFromLocalDBUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    if (!it.onSuccess.isNullOrEmpty()) {
                        for (i in ZERO until it.onSuccess.size) {
                            getQrCodeListItemLocalDB.add(it.onSuccess[i])
                            qrCodeListLocalDB.add(it.onSuccess[i].unique_qrcode_uuid)
                        }
                    }
                }

                is QrCodeListFromLocalDBUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
    }

    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(
                surfaceWidth, surfaceHeight
            )
            .setAutoFocusEnabled(true)
            .build()

        binding.ivZoom.isEnabled = true
        binding.ivTorch.isEnabled = true

        imageCapture = ImageCapture.Builder().build()
        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                cameraSource.release()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                var scannedValue = EMPTY_STRING
                val barcodes: SparseArray<Barcode> = detections.detectedItems
                scannedValue = barcodes.valueAt(ZERO).rawValue

                requireActivity().runOnUiThread {
                    if (scannedValue.isNotEmpty()) {
                        cameraSource.stop()
                        /** Observe network connection status only once */
                        viewModel.isNetworkAvailableObserver.observe(viewLifecycleOwner) { isConnected ->
                            if (!isObserved) {
                                handleNetworkConnection(isConnected, scannedValue)
                                isObserved = true
                            }
                        }
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleNetworkConnection(isConnected: Boolean, scannedValue: String) {
        val savedSelectedList =
            MyPreferences.getArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)
        val userLoginDetails = MyPreferences.getUserDetails()
        if (isConnected) {
            viewModel.qrScanCode(scannedValue, savedSelectedList)
            observeScanTicketResponse()
        } else {
            if (scannedValue.isNotEmpty()) {
                val getQrCodeIndex = qrCodeListLocalDB.indexOf(scannedValue)

                /** if we have qrCode in local db */
                if (getQrCodeIndex != -1) {
                    /** get orderTicketId of scanned qr code*/
                    val orderTicketIdQrCode =
                        getQrCodeListItemLocalDB[getQrCodeIndex].order_tickets_id.toString()
                    val orderNumberQrCode =
                        getQrCodeListItemLocalDB[getQrCodeIndex].order_number.toString()

                    if (orderTicketIdQrCode.isNotEmpty()) {
                        val indexOfCheckInListWithTicketId =
                            getCheckInOrderTicketIdListLocalDB.indexOf(orderTicketIdQrCode)

                        if (indexOfCheckInListWithTicketId != -1) {
                            /** check isScanned true/false */
                            if (getCheckInItemListLocalDB[indexOfCheckInListWithTicketId].is_checked_in == true) {
                                viewModel.insertScanLogOffline(
                                    ScanLog(
                                        "Already scanned",
                                        userLoginDetails?.data?.event?.id,
                                        false,
                                        "",
                                        Utility.getDeviceUUID().toString(),
                                        userLoginDetails?.data?.event?.name,
                                        orderNumberQrCode,
                                        orderTicketIdQrCode.toInt(),
                                        scannedValue,
                                        TimePickerUtility.getCurrentDateTimeForServer(),
                                        userLoginDetails?.data?.scan_key_id,
                                        true,
                                        "",
                                        "Offline",
                                        ""
                                    )
                                )
                                viewModel._qrScanState.value = QrScanUIState.IsLoading(false)
                                viewModel._qrScanState.value =
                                    QrScanUIState.OnFailure("Already scanned")
                                navigateToStatusTicket()

                            } else {
                                getCheckInItemListLocalDB[indexOfCheckInListWithTicketId].is_checked_in =
                                    true
                                viewModel.insertCheckInDataOfflineScan(
                                    getCheckInItemListLocalDB[indexOfCheckInListWithTicketId]
                                )
                                viewModel.insertScanLogOffline(
                                    ScanLog(
                                        "Scanned successfully",
                                        userLoginDetails?.data?.event?.id,
                                        false,
                                        "",
                                        Utility.getDeviceUUID().toString(),
                                        userLoginDetails?.data?.event?.name,
                                        orderNumberQrCode,
                                        orderTicketIdQrCode.toInt(),
                                        scannedValue,
                                        TimePickerUtility.getCurrentDateTimeForServer(),
                                        userLoginDetails?.data?.scan_key_id,
                                        true,
                                        "",
                                        "Offline",
                                        ""
                                    )
                                )
                                viewModel._qrScanState.value = QrScanUIState.OnSuccess(
                                    QrScanResponse(
                                        NameData(""),
                                        "200",
                                        "Scanned successfully",
                                        200
                                    )
                                )
                                navigateToStatusTicket()
                            }
                        } else {
                            viewModel.insertCheckInDataOfflineScan(
                                CheckInData(
                                    userLoginDetails?.data?.event?.id,
                                    true,
                                    false,
                                    "",
                                    Utility.getDeviceUUID(),
                                    userLoginDetails?.data?.event?.name,
                                    orderNumberQrCode,
                                    orderTicketIdQrCode.toInt(),
                                    TimePickerUtility.getCurrentDateTimeForServer(),
                                    userLoginDetails?.data?.scan_key_id
                                )
                            )
                            viewModel.insertScanLogOffline(
                                ScanLog(
                                    "Scanned successfully",
                                    userLoginDetails?.data?.event?.id,
                                    false,
                                    "",
                                    Utility.getDeviceUUID().toString(),
                                    userLoginDetails?.data?.event?.name,
                                    orderNumberQrCode,
                                    orderTicketIdQrCode.toInt(),
                                    scannedValue,
                                    TimePickerUtility.getCurrentDateTimeForServer(),
                                    userLoginDetails?.data?.scan_key_id,
                                    true,
                                    "",
                                    "Offline",
                                    ""
                                )
                            )
                            viewModel.getCheckInDataFromLocalDB()
                            observeCheckInDataFromLocalDB()
                            viewModel._qrScanState.value = QrScanUIState.OnSuccess(
                                QrScanResponse(
                                    NameData(""),
                                    "200",
                                    "Scanned successfully",
                                    200
                                )
                            )
                            navigateToStatusTicket()
                        }
                    }
                } else {
                    viewModel.insertScanLogOffline(
                        ScanLog(
                            "Invalid Ticket",
                            userLoginDetails?.data?.event?.id,
                            false,
                            "",
                            Utility.getDeviceUUID().toString(),
                            userLoginDetails?.data?.event?.name,
                            "",
                            null,
                            scannedValue,
                            TimePickerUtility.getCurrentDateTimeForServer(),
                            userLoginDetails?.data?.scan_key_id,
                            false,
                            "",
                            "Offline",
                            ""
                        )
                    )
                    viewModel._qrScanState.value = QrScanUIState.OnFailure("Invalid Ticket")
                    navigateToStatusTicket()
                }
            }
        }
    }

    private fun observeScanTicketResponse() {
        viewModel.observeQrScanResponse.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    viewModel.getScannedTicketData()
                    navigateToStatusTicket()
                }

                is QrScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    navigateToStatusTicket()
                }
            }
        }
    }

    private fun navigateToStatusTicket() {
        findNavController().navigate(R.id.ticketScannedStatusFragment)
    }

    private fun askForCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getScannedTicketData() {
        viewModel.getScannedTicketData()
        viewModel.observeTicketScannedResponse.observe(viewLifecycleOwner) {
            when (it) {
                is QrScannedTicketUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScannedTicketUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    val res = it.onSuccess.data
                    viewModel.totalScanned = res?.total_scanned ?: ZERO
                    viewModel.totalAccepted = res?.total_accepted ?: ZERO
                    viewModel.totalRejected = res?.total_rejected ?: ZERO
                    binding.tvTotalScanned.text =
                        getString(R.string.total_scanned) + VERTICAL_DOTS + res?.total_scanned.toString()
                    binding.tvAccepted.text =
                        getString(R.string.accepted) + VERTICAL_DOTS + res?.total_accepted.toString()
                    binding.tvRejected.text =
                        getString(R.string.rejected) + VERTICAL_DOTS + res?.total_rejected.toString()
                }

                is QrScannedTicketUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    if (it.onFailure == UNAUTHORIZED_USER.toString()) {
                        expireSession()
                    }
                }
            }
        }
    }

    private fun expireSession() {
        val title = requireContext().getString(R.string.session_expired)
        val message = requireContext().getString(R.string.your_session_has_been_expired)
        val builder = DialogUtils.customAlertDialog(requireContext(),title, message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            Utility.clearLocalDatabase(requireActivity())
            MyPreferences.clearpref()
            findNavController().navigateWithClearNavGraph(
                R.id.nested_qr_scan_nav_graph,
                R.id.loginScanModuleFragment
            )
        }
        val mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    @SuppressLint("MissingPermission")
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setupControls()
                if (this::cameraSource.isInitialized) {
                    cameraSource.start(binding.surfaceView.holder)
                }
            } else {
                SnackBarUtil.showCustomSnackBar(
                    binding.root, getString(R.string.cameraPermissionRequired)
                )
            }
        }

    private fun setFlashMode(mode: String) {
        val declaredFields = CameraSource::class.java.declaredFields
        for (field in declaredFields) {
            if (field.type == Camera::class.java) {
                field.isAccessible = true
                if (this::cameraSource.isInitialized) {
                    val camera = field.get(cameraSource) as Camera?
                    if (camera != null) {
                        try {
                            val params = camera.parameters
                            params.flashMode = mode
                            camera.parameters = params
                            field.set(cameraSource, camera)
                        } catch (e: Exception) {
                            print(e.printStackTrace())
                        }
                        break
                    }
                }
            }
        }
    }

    private fun setCameraZoom(cameraSource: CameraSource, zoomFactor: Float) {
        val cameraField =
            cameraSource::class.java.declaredFields.firstOrNull { it.type == Camera::class.java }
        cameraField?.let {
            it.isAccessible = true
            val camera = it.get(cameraSource) as Camera?
            camera?.let { cam ->
                val params = cam.parameters
                if (params.isZoomSupported) {
                    val maxZoom = params.maxZoom
                    val zoomSetting = (zoomFactor * maxZoom).toInt()
                    params.zoom = zoomSetting
                    cam.parameters = params
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isObserved = false
    }

    private fun enableNfcSettingDialog() {
        val title = requireContext().getString(R.string.required_nfc)
        val message = requireContext().getString(R.string.to_continue_please_enable_nfc)
        val builder = DialogUtils.customAlertDialog(requireContext(),title, message)
        builder.setPositiveButton("OK") { dialog, _ ->
            startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
        }
        val mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(true)
        mDialog.show()
    }

    private fun openScanReportBottomSheet() {
        if (isDialogVisible) return // Prevent opening multiple dialogs
        isDialogVisible = true
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutEndScanBottomDialogBinding.inflate(layoutInflater)
        dialogView.apply {
            tvTitle.text = getString(R.string.end_scan_with_mark)
            tvDescription.text = getString(R.string.are_you_sure_end_scan)
        }
        dialogView.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.btnYes.setOnClickListener {
            findNavController().navigate(R.id.action_scanBottomQRScanFragment_to_qrScanReportFragment)
            cameraSource.stop()
            viewModel.onContinueClick.value = R.id.qrScanReportFragment
            dialog.dismiss()
        }
        dialogView.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnDismissListener {
            isDialogVisible = false
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        isQrButtonEnable(true)
    }

}