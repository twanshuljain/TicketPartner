package com.example.ticketpartner.scan_module.presentation

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.isNotEmpty
import androidx.fragment.app.Fragment
import com.example.ticketpartner.common.SELECT_SCAN_TICKET_ARRAY
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentScanBottomNavQRScanBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class ScanBottomQRScanFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavQRScanBinding
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private val requestCodeCameraPermission = 1001
    private var aniSlide: Animation? = null

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
        initCameraPermission()

        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                selectedTicketTypeList = it.getParcelableArrayList(
                    SELECT_SCAN_TICKET_ARRAY,
                    String::class.java
                ) as ArrayList<String>
            } else {
                selectedTicketTypeList =
                    it.getParcelableArrayList<Parcelable>(SELECT_SCAN_TICKET_ARRAY) as ArrayList<String>
            }
        }
        Log.e("TAG", "selectedTicketNameList: ${selectedTicketTypeList} ")
        initView()
    }

    private fun initView() {

    }

    private fun initCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) askForCameraPermission() else setupControls()

        //   startAnimation()
    }

    private fun setupControls() {
        /*
          binding.surfaceView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
              override fun onGlobalLayout() {
                  // Get the width and height of the SurfaceView
                   surfaceWidth = binding.surfaceView.width
                   surfaceHeight = binding.surfaceView.height


                  // Remove the listener to avoid multiple calls
                  binding.surfaceView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                  // Now you can use surfaceWidth and surfaceHeight as needed
              }
          })*/

        barcodeDetector =
            BarcodeDetector.Builder(requireContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(
                surfaceWidth, surfaceHeight
            )
            .setAutoFocusEnabled(true) //you should add this feature
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
                val barcodes: SparseArray<Barcode> = detections.detectedItems
                if (barcodes.isNotEmpty()) {
                    val scannedValue = barcodes.valueAt(ZERO).rawValue
                    SnackBarUtil.showSuccessSnackBar(binding.root, scannedValue)
                }
            }
        })
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

}