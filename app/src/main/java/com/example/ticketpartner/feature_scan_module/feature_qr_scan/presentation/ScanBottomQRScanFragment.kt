package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_DOTS
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST
import com.example.ticketpartner.databinding.FragmentScanBottomNavQRScanBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.GetCheckInDataLocalDBUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrCodeListFromLocalDBUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NetworkConnectionLiveData
import com.example.ticketpartner.utils.Utility.observeOnce
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException

class ScanBottomQRScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavQRScanBinding
    private val viewModel: QrScanViewModel by activityViewModels()
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
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavQRScanBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initCameraPermission()
        getScannedTicketData()
    }

    private fun initView() {
        getQrCodeListItemLocalDB.clear()
        qrCodeListLocalDB.clear()
        getCheckInItemListLocalDB.clear()
        getCheckInOrderTicketIdListLocalDB.clear()

        viewModel.insertCheckInDataOfflineScan(
            CheckInData(
                4,
                false,
                false,
                "testRaj",
                "rajneesh",
                "staff",
                "12000",
                1234,
                "",
                5
            )
        )

        observeQrCodeListFromLocalDB()
        observeCheckInDataFromLocalDB()
        binding.ivZoom.isEnabled = false
        binding.ivTorch.isEnabled = false

        viewModel.eventName.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
        }

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner) {
            val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
            subTitle?.visibility = View.VISIBLE
            subTitle?.text = it
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
            openImagePickerBottomSheet()
        }

    }

    private fun observeCheckInDataFromLocalDB() {
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
                is QrCodeListFromLocalDBUIState.IsLoading -> {}
                is QrCodeListFromLocalDBUIState.OnSuccess -> {
                    if (!it.onSuccess.isNullOrEmpty()) {
                        for (i in ZERO until it.onSuccess.size) {
                            getQrCodeListItemLocalDB.add(it.onSuccess[i])
                            qrCodeListLocalDB.add(it.onSuccess[i].unique_qrcode_uuid)
                        }
                    }
                }

                is QrCodeListFromLocalDBUIState.OnFailure -> {}
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

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                var scannedValue = EMPTY_STRING
                val barcodes: SparseArray<Barcode> = detections.detectedItems
                scannedValue = barcodes.valueAt(ZERO).rawValue

                var count = 0
                requireActivity().runOnUiThread {
                    scannedValue = "93027513091069"
                    if (scannedValue.isNotEmpty()) {
                        cameraSource.stop()
                        val savedSelectedList =
                            MyPreferences.getArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)
                        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
                        networkConnectionLiveData.observeOnce(
                            viewLifecycleOwner,
                            Observer { isConnected ->

                                if (isConnected) {
                                    viewModel.qrScanCode(scannedValue, savedSelectedList)
                                    observeScanTicketResponse()
                                    Toast.makeText(requireContext(), "online", Toast.LENGTH_SHORT)
                                        .show()

                                } else {

                                    if (scannedValue.isNotEmpty()) {
                                        val getQrCodeIndex = qrCodeListLocalDB.indexOf(scannedValue)

                                        /** if we have qrCode in local db */
                                        if (getQrCodeIndex != -1) {

                                            /** get orderTicketId of scanned qr code*/
                                            val orderTicketIdQrCode =
                                                getQrCodeListItemLocalDB[getQrCodeIndex].order_tickets_id.toString()

                                            if (orderTicketIdQrCode.isNotEmpty()) {
                                                val indexOfCheckInListWithTicketId =
                                                    getCheckInOrderTicketIdListLocalDB.indexOf(
                                                        orderTicketIdQrCode
                                                    )

                                                /** check isScanned true/false */
                                                if (getCheckInItemListLocalDB[indexOfCheckInListWithTicketId].is_checked_in == true) {
                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Already scan",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    getCheckInItemListLocalDB[indexOfCheckInListWithTicketId].is_checked_in = true
                                                    viewModel.insertCheckInDataOfflineScan(
                                                   getCheckInItemListLocalDB[indexOfCheckInListWithTicketId]
                                                )

                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Scanned successfully!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                    scannedValue = EMPTY_STRING
                                }
                            })
                    }
                }
            }
        })
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
                    binding.tvTotalScanned.text =
                        getString(R.string.total_scanned) + VERTICAL_DOTS + res?.total_scanned.toString()
                    binding.tvAccepted.text =
                        getString(R.string.accepted) + VERTICAL_DOTS + res?.total_accepted.toString()
                    binding.tvRejected.text =
                        getString(R.string.rejected) + VERTICAL_DOTS + res?.total_rejected.toString()
                }

                is QrScannedTicketUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
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
                SnackBarUtil.showErrorSnackBar(
                    binding.root,
                    getString(R.string.cameraPermissionRequired)
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
                            Log.e("setFlashMode", "Error setting camera flash mode", e)
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

    private fun openImagePickerBottomSheet() {
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
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }
}