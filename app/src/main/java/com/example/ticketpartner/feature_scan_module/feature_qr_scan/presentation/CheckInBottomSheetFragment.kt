package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentCheckInBottomSheetBinding
import com.example.ticketpartner.databinding.LayoutScanCheckinAllowedOrdersDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.Item
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanCheckedInUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CheckInBottomSheetFragment(
    rootContext: ConstraintLayout,
    orderId: String,
    searDetailsResponse: ArrayList<Item>
) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCheckInBottomSheetBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: ScanCheckInAdapter
    private var checkedOrderIdList = ArrayList<Long>()
    private val orderId = orderId
    private val context = rootContext
    private val searchDetailsResponse = searDetailsResponse
    private var itemPosition = ArrayList<Int>()
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
        itemPosition.clear()
        adapter = ScanCheckInAdapter(requireContext(), emptyList(), ::checkedOrderId, ::position)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        setAdapter(searchDetailsResponse)

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
                    for (i in ZERO until itemPosition.size) {
                        searchDetailsResponse[itemPosition[i]].is_checked_in = true
                    }

                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(context, it.onSuccess.message.toString())
                    val bottomSheetDialog = dialog as? BottomSheetDialog
                    bottomSheetDialog?.dismiss()
                    logoutDialog()
                }

                is QrScanCheckedInUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(context, it.onFailure)
                }
            }
        }
    }

    private fun setAdapter(data: List<Item?>?) {
        adapter =
            data?.let { ScanCheckInAdapter(requireContext(), it, ::checkedOrderId, ::position) }!!
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
    }

    private fun position(position: Int) {
        itemPosition.add(position)
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

    @SuppressLint("SetTextI18n")
    private fun logoutDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutScanCheckinAllowedOrdersDialogBinding.inflate(layoutInflater)
        dialogView.apply {
            tvTitle.text = getString(R.string.allowed_orders)
            tvDescription.text =
                "${checkedOrderIdList.size} ${requireContext().getString(R.string.people_are_allowed_to_enter)}"
        }
        dialogView.btnYes.setOnClickListener {
            context.findNavController().popBackStack()
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