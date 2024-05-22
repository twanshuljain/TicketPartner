package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

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
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.VERTICAL_DOTS
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST
import com.example.ticketpartner.databinding.FragmentScanBottomNavQRScanBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrCodeListFromLocalDBUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.utils.DialogProgressUtil
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
    private val requestCodeCameraPermission = 1001
    private var aniSlide: Animation? = null
    private var isTorchOn = false
    private var selectedTicketTypeList = ArrayList<String>()
    private var surfaceWidth: Int = 340
    private var surfaceHeight: Int = 340
    private var currentZoom = 0f
    private lateinit var imageCapture: ImageCapture
    private var getQrCodeListLocalDB = ArrayList<DataItems>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        getQrCodeListLocalDB.clear()
        observeQrCodeListFromLocalDB()

        viewModel.eventName.observe(viewLifecycleOwner){
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
        }

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner){
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
                binding.ivTorch.setImageResource(R.drawable.ic_temp_selected_torch)
            }
        }

        binding.ivZoom.setOnClickListener {
            if (currentZoom == 0f) {
                currentZoom = 0.5f
                binding.ivZoom.setImageResource(R.drawable.ic_temp_selected_zoom)
                setCameraZoom(cameraSource, 0.5f)
            } else if (currentZoom == 0.5f) {
                currentZoom = 1f
                setCameraZoom(cameraSource, 1f)
                binding.ivZoom.setImageResource(R.drawable.ic_temp_selected_zoom)
            } else if (currentZoom == 1f) {
                currentZoom = 0f
                setCameraZoom(cameraSource, 0f)
                binding.ivZoom.setImageResource(R.drawable.ic_1x_zoom_grey)
            }
        }

        binding.btnEndScan.setOnClickListener {
            openImagePickerBottomSheet()
        }

        // viewModel.qrScanCode("244447224741818",selectedTicketTypeList)
    }

    private fun initCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) askForCameraPermission() else setupControls()

    }

    private fun observeQrCodeListFromLocalDB() {
        viewModel.getQrCodeListFromLocalDB.observe(viewLifecycleOwner){
            when(it){
                is QrCodeListFromLocalDBUIState.IsLoading -> {
                    Log.e("TAG", "observeQrCodeListFromLocalDB: loading ", )
                }
                is QrCodeListFromLocalDBUIState.OnSuccess -> {
                    //  getQrCodeListLocalDB.addAll(it.onSuccess)
                    for (i in ZERO until it.onSuccess.size){
                        getQrCodeListLocalDB.add(it.onSuccess[i])
                    }
                }
                is QrCodeListFromLocalDBUIState.OnFailure -> {
                    Log.e("TAG", "observeQrCodeListFromLocalDB: Error.. ", )
                }
            }
        }

        for (i in 0 until getQrCodeListLocalDB.size){
            Log.e("TAG", "observeQrCodeListFromLocalDB: Success! ${getQrCodeListLocalDB[i]} ", )
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
                var scannedValue = ""
                val barcodes: SparseArray<Barcode> = detections.detectedItems
                scannedValue = barcodes.valueAt(0).rawValue

                //Don't forget to add this line printing value or finishing activity must run on main thread
                requireActivity().runOnUiThread {
                    if (scannedValue.isNotEmpty()) {
                        cameraSource.stop()
                        val savedSelectedList = MyPreferences.getArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)
                        viewModel.qrScanCode(scannedValue, savedSelectedList)
                        observeScanTicketResponse()
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
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                // Permission was denied
                // Handle the case where the user denies the permission
            }
        }
    }

    /*

        private fun applyZoom(scaleFactor: Float) {
            if (currentZoom != scaleFactor) {
                matrix.setScale(scaleFactor, scaleFactor, imageView.width / 2f, imageView.height / 2f)
                imageView.imageMatrix = matrix
                currentZoom = scaleFactor
            }
        }
    */

    private fun setFlashMode(mode: String) {
        val declaredFields = CameraSource::class.java.declaredFields

        for (field in declaredFields) {
            if (field.type == Camera::class.java) {
                field.isAccessible = true
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

    fun setCameraZoom(cameraSource: CameraSource, zoomFactor: Float) {
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