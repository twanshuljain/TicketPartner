package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentCheckInBottomSheetBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.Item
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanCheckedInUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanOrderDetailsUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CheckInBottomSheetFragment(rootContext: ConstraintLayout, orderId: String) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCheckInBottomSheetBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: ScanCheckInAdapter
    private var checkedOrderIdList = ArrayList<Long>()
    private val orderId = orderId
    private val context = rootContext
   private var responseListSize = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckInBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ScanCheckInAdapter(requireContext(), emptyList(), ::checkedOrderId)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getOrderDetailsResponse(orderId)
        observeOrderDetailsData()

        binding.btnYes.setOnClickListener {
            viewModel.getCheckInOrder(checkedOrderIdList, orderId)
            observeCheckedInResponse()
        }

        binding.btnNo.setOnClickListener {
            val bottomSheetDialog = dialog as? BottomSheetDialog
            bottomSheetDialog?.dismiss()
        }
    }

    private fun observeCheckedInResponse() {
        viewModel.observeScanCheckedInData.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanCheckedInUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanCheckedInUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(context, it.onSuccess.message.toString())
                    val bottomSheetDialog = dialog as? BottomSheetDialog
                    bottomSheetDialog?.dismiss()
                }

                is QrScanCheckedInUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun observeOrderDetailsData() {
        viewModel.observeScanOrderDetailsData.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanOrderDetailsUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanOrderDetailsUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    setAdapter(it.onSuccess.data)
                   responseListSize = it.onSuccess.data?.size!! ?:0
                }

                is QrScanOrderDetailsUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setAdapter(data: List<Item?>?) {
        adapter = data?.let { ScanCheckInAdapter(requireContext(), it, ::checkedOrderId) }!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun checkedOrderId(list: ArrayList<Long>) {
        checkedOrderIdList = list
    }

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheetInternal =
                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.layoutParams?.height = 600 // or specify a fixed size
        }
    }

}