package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentScanBottomNavSearchBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanBottomNavSearchFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavSearchBinding
    private lateinit var adapter: ScanSearchOrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBottomNavSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
       adapter = ScanSearchOrderAdapter(getSearchListResponse(),::isItemClicked)
        binding.rvScanOrder.adapter = adapter
        binding.rvScanOrder.setHasFixedSize(true)

    }

    private fun getSearchListResponse(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 0 until 20) {
            list.add("Rebecca Young")
        }
        return list
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
            findNavController().navigate(R.id.qrScanReportFragment)
            dialog.dismiss()
        }
        dialogView.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
    }
    private fun isItemClicked(b: Boolean) {
        if (b){
            //openImagePickerBottomSheet()
            findNavController().navigate(R.id.scanSearchedOrderDetailsFragment)
        }
    }
}


