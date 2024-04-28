package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentScanBottomNavSearchBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.DataList
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanSearchItemUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanBottomNavSearchFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavSearchBinding
    private lateinit var adapter: ScanSearchOrderAdapter
    private val viewModel: QrScanViewModel by activityViewModels()
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
        observeSearchItemResponse("b5fac2c0-d689-47fd-900b-4faa6f7ec8e4")
    }

    private fun observeSearchItemResponse(orderId: String) {
        viewModel.getSearchData(orderId)
        viewModel.observeScanSearchData.observe(viewLifecycleOwner){
            when(it){
                is QrScanSearchItemUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is QrScanSearchItemUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.onSuccess.data?.let {list ->
                        setAdapter(list)
                    }
                }
                is QrScanSearchItemUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
    }

    private fun setAdapter(data: List<DataList?>) {
        data?.let {
            adapter = ScanSearchOrderAdapter(it,::isItemClicked)
            binding.rvScanOrder.adapter = adapter
            binding.rvScanOrder.setHasFixedSize(true)
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


