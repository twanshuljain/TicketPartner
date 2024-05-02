package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentCheckInBottomSheetBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanOrderDetailsUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CheckInBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCheckInBottomSheetBinding
    private val viewModel: QrScanViewModel by activityViewModels()
private lateinit var adapter: ScanCheckInAdapter
    private var response = ArrayList<String>()
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
        viewModel.getOrderDetailsResponse("")
        observeOrderDetailsData()

        for (i in 0..10){
            response.add(i,"Order ID: 216574325")
        }

        adapter = ScanCheckInAdapter(requireContext(),response)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun observeOrderDetailsData() {
        viewModel.observeScanOrderDetailsData.observe(viewLifecycleOwner){
            when(it){
                is QrScanOrderDetailsUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is QrScanOrderDetailsUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                }
                is QrScanOrderDetailsUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheetInternal = it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.layoutParams?.height = 600 // or specify a fixed size
        }
    }

}