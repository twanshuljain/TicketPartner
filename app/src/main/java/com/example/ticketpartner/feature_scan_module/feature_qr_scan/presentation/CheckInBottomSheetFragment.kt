package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.FragmentCheckInBottomSheetBinding
import com.example.ticketpartner.databinding.LayoutScanCheckinAllowedOrdersDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.Item
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanCheckedInUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NetworkConnectionLiveData
import com.example.ticketpartner.utils.TimePickerUtility
import com.example.ticketpartner.utils.Utility
import com.example.ticketpartner.utils.Utility.observeOnce
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
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    private lateinit var adapter: ScanCheckInAdapter
    private var checkedOrderIdList = ArrayList<Long>()
    private val orderId = orderId
    private val context = rootContext
    private val searchDetailsResponse = searDetailsResponse
    private var itemPosition = ArrayList<Int>()
    private var orderList = ArrayList<String>()
    private var searchItemData = ArrayList<SearchData>()
    private var searchDataForUpdate = ArrayList<SearchData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckInBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPosition.clear()
        adapter = ScanCheckInAdapter(requireContext(), searchDetailsResponse, ::checkedOrderId, ::position)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)

        if (searchDetailsResponse.isNotEmpty()){
            setAdapter(searchDetailsResponse)
        }

        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
        viewModel.selectedSearchOrderListData.observe(viewLifecycleOwner) {
            orderList.clear()
            searchItemData.clear()
            for (i in ZERO until it.size) {
                orderList.add(it[i].order_ticket_id.toString())
                searchItemData.add(it[i])
            }
        }

        binding.btnCheckIn.setOnClickListener {
            if (checkedOrderIdList.size > ZERO) {
                val userLoginDetails = MyPreferences.getUserDetails()
                networkConnectionLiveData.observeOnce(
                    viewLifecycleOwner, Observer { isConnected ->
                        if (isConnected) {
                            viewModel.getCheckInOrder(checkedOrderIdList, orderId)
                            observeCheckedInResponse()
                        } else {
                            searchDataForUpdate.clear()
                            for (i in ZERO until checkedOrderIdList.size) {
                                val index = orderList.indexOf(checkedOrderIdList[i].toString())
                                searchItemData[index].is_checked_in = true
                                searchDataForUpdate.add(searchItemData[index])
                            }

                            for (data in searchDataForUpdate) {
                                viewModel.insertSearchDataOfflineScan(data)
                                viewModel.insertScanLogOffline(
                                    ScanLog(
                                        "Scanned successfully",
                                        userLoginDetails?.data?.event?.id,
                                        false,
                                        "",
                                        Utility.getDeviceUUID().toString(),
                                        userLoginDetails?.data?.event?.name,
                                        data.order_number,
                                        data.order_ticket_id,
                                        "",
                                        TimePickerUtility.getCurrentDateTimeForServer(),
                                        userLoginDetails?.data?.scan_key_id,
                                        true,
                                        "",
                                        "Offline",
                                        ""
                                    )
                                )
                            }

                            val bottomSheetDialog = dialog as? BottomSheetDialog
                            bottomSheetDialog?.dismiss()
                            dialogAllowPeople()
                        }
                    })
            }else{
                SnackBarUtil.showErrorSnackBar(context,getString(R.string.please_select_order_id))
            }
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
                        dialogAllowPeople()
                    }

                    is QrScanCheckedInUIState.OnFailure -> {
                        DialogProgressUtil.dismiss()
                        SnackBarUtil.showErrorSnackBar(context, it.onFailure)
                    }
                }
            }
        }

        private fun setAdapter(data: List<Item?>?) {
            val uniqueList = data?.toSet()
            val uniqueItemList: List<Item?> = uniqueList?.toList() ?: emptyList()
            adapter =
                data?.let {
                    ScanCheckInAdapter(
                        requireContext(),
                        uniqueItemList,
                        ::checkedOrderId,
                        ::position
                    )
                }!!
            binding.recyclerView.adapter = adapter
            binding.recyclerView.setHasFixedSize(true)
        }

        private fun position(position: Int) {
            itemPosition.add(position)
        }

        private fun checkedOrderId(list: ArrayList<Long>) {
           /* viewModel.isOnlineMode.observe(viewLifecycleOwner) {
                if (!it) {
                    searchDataForUpdate.clear()
                    for (i in ZERO until list.size) {
                        val index = orderList.indexOf(list[i].toString())
                        searchItemData[index].is_checked_in = true
                        searchDataForUpdate.add(searchItemData[index])
                    }
                }
            }*/
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
        private fun dialogAllowPeople() {
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

    }

    }