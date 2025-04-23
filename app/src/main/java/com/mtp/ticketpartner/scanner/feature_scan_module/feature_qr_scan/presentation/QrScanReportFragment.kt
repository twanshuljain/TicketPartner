package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.HYPHEN_CHAR
import com.mtp.ticketpartner.scanner.common.SnackBarUtil
import com.mtp.ticketpartner.scanner.common.VERTICAL_POLE
import com.mtp.ticketpartner.scanner.common.ZERO
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.ticketpartner.scanner.databinding.FragmentQrScanReportBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.DeleteScanLogDataUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanLogOfflineUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanReportDataOfflineUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanReportTicketListOfflineUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanDataServerUIState
import com.mtp.ticketpartner.scanner.utils.DialogProgressUtil
import com.mtp.ticketpartner.scanner.utils.NetworkConnectionLiveData
import com.mtp.ticketpartner.scanner.utils.getFormattedStartDateForEvent
import com.mtp.ticketpartner.scanner.utils.getFormattedTimeForEvent

class QrScanReportFragment : Fragment() {
    private lateinit var binding: FragmentQrScanReportBinding
    private lateinit var adapter: ScanReportTicketNameAdapter
    private val viewModel: QrScanViewModel by activityViewModels()
    private val scanLogDataOffline = ArrayList<ScanLog>()
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrScanReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeScanReportDataLocalDB()

        viewModel.isNetworkAvailableObserver.observe(viewLifecycleOwner) {
            if (it) {
                try {
                    viewModel.getScanReportAllData("all")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                observeScanReportAllData()
                observeScanReportOffline()

            } else {
                //binding.btnUploadDataServer.visibility = View.GONE
                observeScanReportOffline()
            }
        }
    }

    private fun observeScanReportDataLocalDB() {
        viewModel.getScanReportDataFromLocalDB()
        viewModel.getScanReportTicketListOffline()

        viewModel.getScanReportDataFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetScanReportDataOfflineUIState.IsLoading -> {}
                is GetScanReportDataOfflineUIState.OnSuccess -> {
                    val value = it.onSuccess[ZERO]
                    setProgressBarForAll(value)
                }

                is GetScanReportDataOfflineUIState.OnFailure -> {}
            }
        }

        viewModel.getScanReportTicketListFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetScanReportTicketListOfflineUIState.IsLoading -> {}
                is GetScanReportTicketListOfflineUIState.OnSuccess -> {
                    it.onSuccess?.let { ticketList ->
                        setAdapter(ticketList)
                    }
                }

                is GetScanReportTicketListOfflineUIState.OnFailure -> {}
            }
        }
    }

    private fun observeScanReportOffline() {
        // viewModel.getScanLogListData()
        scanLogDataOffline.clear()
        viewModel.getScanLogListData()
        viewModel.getScanLogLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetScanLogOfflineUIState.IsLoading -> {}
                is GetScanLogOfflineUIState.OnSuccess -> {
                    scanLogDataOffline.clear()
                    for (i in it.onSuccess) {
                        scanLogDataOffline.add(i)
                    }
                    /*if (scanLogDataOffline.size > ZERO) binding.btnUploadDataServer.visibility =
                        View.VISIBLE else binding.btnUploadDataServer.visibility = View.GONE*/
                }

                is GetScanLogOfflineUIState.OnFailure -> {}
            }
        }
    }


    private fun observeScanReportAllData() {
        viewModel.getScanReportAllData.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanReportAllUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanReportAllUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.onSuccess.data.let {
                        it?.let {
                            val value = InsertScanReportDataResponse(
                                0,
                                it.online,
                                it.physical,
                                it.offline,
                                it.complimentary,
                                it.total_scanned,
                                it.total_accepted,
                                it.total_rejected,
                                it.total_tickets
                            )
                            viewModel.totalScanned = it.total_scanned
                            viewModel.totalAccepted = it.total_accepted
                            viewModel.totalRejected = it.total_rejected
                            setProgressBarForAll(value)
                        }
                    }
                    setAdapter(it.onSuccess.data?.ticket_data)
                }

                is QrScanReportAllUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setProgressBarForAll(data: InsertScanReportDataResponse?) {
        binding.apply {
            data?.total_scanned = viewModel.totalScanned
            data?.total_accepted = viewModel.totalAccepted
            data?.total_rejected = viewModel.totalRejected

            tvAcceptedCount.text = data?.total_accepted.toString()
            tvRejectedCount.text = data?.total_rejected.toString()
            tvOnlineCount.text = data?.online.toString()
            tvPhysicalCount.text = data?.physical.toString()
            tvOfflineCount.text = data?.offline.toString()
            tvComplimentaryCount.text = data?.complimentary.toString()
            tvTotalValue.text = data?.total_tickets.toString()

            tvTotalValue.text = data?.total_tickets.toString()
            tvOutOfValueRejected.text = data?.total_rejected.toString()
            //tvTotalValueRejected.text = "/" + data?.total_tickets.toString()
           // tvTotalValueAccepted.text = "/" + data?.total_tickets.toString()
            tvOutOfValueAccepted.text = data?.total_accepted.toString()


            progressAccepted.progress = data?.total_accepted ?: 0
            progressRejected.progress = data?.total_rejected ?: 0
        }

        /* val acceptedRatio = data?.let { it.total_accepted } ?: 0
         val rejectedRatio = data?.let { it.total_rejected } ?: 0*/


        val acceptedRatio = data?.let { viewModel.totalAccepted } ?: 0
        val rejectedRatio = data?.let { viewModel.totalRejected } ?: 0

        val onlineRatio = data?.let { it.online } ?: 0
        val offlineRatio = data?.let { it.offline } ?: 0
        val complimentaryRatio = data?.let { it.complimentary } ?: 0
        val physicalRation = data?.let { it.physical } ?: 0
        val totalTickets = data?.let { it.total_tickets }

        val progressValues = listOf(
            physicalRation to requireContext().getColor(R.color.light_blue_progress_bar),
            rejectedRatio to requireContext().getColor(R.color.red),
            onlineRatio to requireContext().getColor(R.color.yellow_progress_bar),
            offlineRatio to requireContext().getColor(R.color.dark_grey),
            complimentaryRatio to requireContext().getColor(R.color.orange_progress_bar),
        )

        binding.progress.setProgressValues(progressValues, totalTickets ?: ZERO)

    }

    private fun initView() {
        val userLoginDetails = MyPreferences.getUserDetails()
        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())

        binding.tvEventName.text = userLoginDetails?.data?.event?.name

        val startDate =
            getFormattedStartDateForEvent(userLoginDetails?.data?.event?.event_start_date) + VERTICAL_POLE

        val startEndTime =
            getFormattedTimeForEvent(userLoginDetails?.data?.event?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                userLoginDetails?.data?.event?.event_end_time
            )

        binding.tvDateTime.text = startDate + startEndTime

        val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
        subTitle?.visibility = View.GONE

        val title = activity?.findViewById<AppCompatTextView>(R.id.title)
        title?.text = getString(R.string.scanReport)

        //  adapter = ScanReportTicketNameAdapter(emptyList())
        binding.includeTitle.ivBack.visibility = View.GONE

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner) {
            binding.tvDateTime.text = it
        }

        viewModel.eventName.observe(viewLifecycleOwner) {
            binding.tvEventName.text = it
        }

        binding.btnAll.setOnClickListener {
            viewModel.getScanReportAllData("all")
            binding.apply {
                progress.visibility = View.VISIBLE
                progressAccepted.visibility = View.GONE
                progressRejected.visibility = View.GONE
                tvOutOfValueAccepted.visibility = View.GONE

                tvTotalTicket.visibility = View.VISIBLE
                tvTotalValue.visibility = View.VISIBLE

                tvTotalTicketAccepted.visibility = View.GONE
                //tvTotalValueAccepted.visibility = View.GONE

                tvTotalTicketRejected.visibility = View.GONE
                //tvTotalValueRejected.visibility = View.GONE
                tvOutOfValueRejected.visibility = View.GONE

                btnAll.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAll.setTextColor(requireContext().getColor(R.color.white))
                btnAccepted.setTextColor(requireContext().getColor(R.color.orange))
                btnRejected.setTextColor(requireContext().getColor(R.color.orange))
                clAllTicketCount.visibility = View.VISIBLE

            }
        }

        binding.btnAccepted.setOnClickListener {
            viewModel.getScanReportAllData("accepted")
            binding.apply {
                progressAccepted.visibility = View.VISIBLE
                progress.visibility = View.GONE
                progressRejected.visibility = View.GONE

                tvTotalTicket.visibility = View.GONE
                tvTotalValue.visibility = View.GONE

                tvTotalTicketAccepted.visibility = View.VISIBLE
                //tvTotalValueAccepted.visibility = View.VISIBLE
                tvOutOfValueAccepted.visibility = View.VISIBLE

                tvTotalTicketRejected.visibility = View.GONE
                //tvTotalValueRejected.visibility = View.GONE
                tvOutOfValueRejected.visibility = View.GONE

                btnAll.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAll.setTextColor(requireContext().getColor(R.color.orange))
                btnAccepted.setTextColor(requireContext().getColor(R.color.white))
                btnRejected.setTextColor(requireContext().getColor(R.color.orange))
                clAllTicketCount.visibility = View.GONE
            }
        }
        binding.btnRejected.setOnClickListener {
            viewModel.getScanReportAllData("rejected")
            binding.apply {
                progressAccepted.visibility = View.GONE
                progress.visibility = View.GONE
                tvOutOfValueAccepted.visibility = View.GONE
                progressRejected.visibility = View.VISIBLE

                tvTotalTicket.visibility = View.GONE
                tvTotalValue.visibility = View.GONE

                tvTotalTicketAccepted.visibility = View.GONE
                //tvTotalValueAccepted.visibility = View.GONE

                tvTotalTicketRejected.visibility = View.VISIBLE
               // tvTotalValueRejected.visibility = View.VISIBLE
                tvOutOfValueRejected.visibility = View.VISIBLE

                btnAll.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnAll.setTextColor(requireContext().getColor(R.color.orange))
                btnAccepted.setTextColor(requireContext().getColor(R.color.orange))
                btnRejected.setTextColor(requireContext().getColor(R.color.white))
                clAllTicketCount.visibility = View.GONE
            }
        }

        /** upload store locally scan data to server */
        /** Open this for btnUploadDataServer functionality */
        /*binding.btnUploadDataServer.setOnClickListener {
            if (viewModel.isNetworkAvailable) {
                if (scanLogDataOffline.size > ZERO) {
                    viewModel.uploadScanLogDataOnServer(
                        SendScanLogOfflineRequest(
                            scanLogDataOffline
                        )
                    )
                    observeUploadDataOnServerResponse()
                } else {
                    SnackBarUtil.showCustomSnackBar(
                        binding.root,
                        getString(R.string.you_do_not_have_data)
                    )
                }
            } else {
                SnackBarUtil.showCustomSnackBar(
                    binding.root,
                    getString(R.string.check_network_availability)
                )
            }
        }*/
    }

    /** Observe response of upload store locally scan data to server */
    private fun observeUploadDataOnServerResponse() {
        viewModel.uploadScanLogDataOnServer.observe(viewLifecycleOwner) {
            when (it) {
                is UploadScanDataServerUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is UploadScanDataServerUIState.OnSuccess -> {
                    scanLogDataOffline.clear()
                    viewModel.getScanLogListData()
                    SnackBarUtil.showCustomSnackBar(
                        binding.root,
                        it.onSuccess.message.toString(),
                        true
                    )
                    viewModel.deleteScanLogDataFromLocalDB()
                    observeScanLogDeleteResponse()
                    viewModel.deleteScanLogDataFromLocalDB
                    viewModel.getScannedTicketData()
                    DialogProgressUtil.dismiss()
                }

                is UploadScanDataServerUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    /** Observe delete scan-log data from local storage */
    private fun observeScanLogDeleteResponse() {
        viewModel.deleteScanLogDataFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is DeleteScanLogDataUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is DeleteScanLogDataUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    viewModel.scanLogListSize = ZERO
                   // binding.btnUploadDataServer.visibility = View.GONE
                }

                is DeleteScanLogDataUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
    }

    private fun setAdapter(getResponseList: List<TicketDataList?>?) {
        adapter = ScanReportTicketNameAdapter(getResponseList)
        binding.rvReportTicketName.adapter = adapter
        binding.rvReportTicketName.setHasFixedSize(true)
    }

}