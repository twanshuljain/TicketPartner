package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.QrScanReportFragment
import com.example.ticketpartner.R
import com.example.ticketpartner.common.VERTICAL_DOTS
import com.example.ticketpartner.databinding.FragmentScanBottomNavQRScanBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.TorchController
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

    private var surfaceWidth: Int = 942
    private var surfaceHeight: Int = 942

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
        binding.ivTorch.setOnClickListener {
            if (isTorchOn) {
                TorchController.turnOff(requireContext())
                isTorchOn = false
            } else {
                TorchController.turnOn(requireContext())
                isTorchOn = true
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


    private fun setupControls() {

        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(
                surfaceWidth, surfaceHeight
            )
            .setAutoFocusEnabled(true)
            .build()


        binding.surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
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
                val list = ArrayList<String>()
                list.add("The Vvip")
                val barcodes: SparseArray<Barcode> = detections.detectedItems
                scannedValue = barcodes.valueAt(0).rawValue

                //Don't forget to add this line printing value or finishing activity must run on main thread
                requireActivity().runOnUiThread {
                    if (scannedValue.isNotEmpty()) {
                        cameraSource.stop()
                        viewModel.qrScanCode(scannedValue,list)
                        observeScanTicketResponse()
                    }
                }
            }
        })
    }

    private fun observeScanTicketResponse(){
        viewModel.observeQrScanResponse.observe(viewLifecycleOwner){
            when(it){
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
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.frameLayout, TicketScannedStatusFragment())
        transaction.addToBackStack(null)
        transaction.commit()
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
            //findNavController().navigate(R.id.qrScanReportFragment)
            scanReportNavigation()
            dialog.dismiss()

        }
        dialogView.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }

    private fun scanReportNavigation() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.frameLayout, QrScanReportFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}