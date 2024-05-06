package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentQrScanReportBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.DataList
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import com.example.ticketpartner.utils.DialogProgressUtil

class QrScanReportFragment : Fragment() {
    private lateinit var binding: FragmentQrScanReportBinding
    private lateinit var adapter: ScanReportTicketNameAdapter
    private val viewModel: QrScanViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrScanReportBinding.inflate(layoutInflater)
        viewModel.getScanReportAllData("all")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        observeScanReportAllData()
    }


    private fun observeScanReportAllData() {
        viewModel.getScanReportAllData.observe(viewLifecycleOwner){
            when(it){
                is QrScanReportAllUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is QrScanReportAllUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.onSuccess.data.let {
                        setProgressBarForAll(it)
                    }

                    setAdapter(it.onSuccess.data?.ticket_data)
                }
                is QrScanReportAllUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setProgressBarForAll(data: DataList?) {
        binding.apply {
            tvAcceptedCount.text = data?.total_accepted.toString()
            tvRejectedCount.text = data?.total_rejected.toString()
            tvOnlineCount.text = data?.online.toString()
            tvPhysicalCount.text = data?.physical.toString()
            tvTotalValue.text = data?.total_tickets.toString()

            tvTotalValue.text = data?.total_tickets.toString()
            tvTotalValueRejected.text = data?.total_tickets.toString()
            tvTotalValueAccepted.text = data?.total_tickets.toString()

            progressAccepted.progress = data?.total_accepted ?:0
            progressRejected.progress = data?.total_rejected ?:0
        }

        val acceptedRatio = data?.let { it.total_accepted } ?:0
        val rejectedRatio = data?.let { it.total_rejected }  ?:0
        val onlineRatio= data?.let { it.online }  ?:0
        val physicalRation  = data?.let { it.physical }  ?:0
        val totalTickets = data?.let { it.total_tickets }

        data?.let {  }

        val progressValues = listOf(
            acceptedRatio to requireContext().getColor(R.color.green_progress_bar),
            rejectedRatio to requireContext().getColor(R.color.orange_progress_bar),
            onlineRatio to requireContext().getColor(R.color.yellow_progress_bar),
            physicalRation to requireContext().getColor(R.color.light_blue_progress_bar)
        )
        data?.total_ticket_ratio?.total?.toInt()
            ?.let { binding.progress.setProgressValues(progressValues, totalTickets?: ZERO) }

    }

    private fun initView() {
        adapter = ScanReportTicketNameAdapter(emptyList())
        binding.includeTitle.ivBack.visibility = View.GONE

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner){
            binding.tvDateTime.text = it
        }

        viewModel.eventName.observe(viewLifecycleOwner){
            binding.tvEventName.text = it
        }

        binding.btnAll.setOnClickListener {
            binding.apply {
                progress.visibility = View.VISIBLE
                progressAccepted.visibility = View.GONE
                progressRejected.visibility = View.GONE

                tvTotalTicket.visibility = View.VISIBLE
                tvTotalValue.visibility = View.VISIBLE

                tvTotalTicketAccepted.visibility = View.GONE
                tvTotalValueAccepted.visibility = View.GONE

                tvTotalTicketRejected.visibility = View.GONE
                tvTotalValueRejected.visibility = View.GONE

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
            binding.apply {
                progressAccepted.visibility = View.VISIBLE
                progress.visibility = View.GONE
                progressRejected.visibility = View.GONE

                tvTotalTicket.visibility = View.GONE
                tvTotalValue.visibility = View.GONE

                tvTotalTicketAccepted.visibility = View.VISIBLE
                tvTotalValueAccepted.visibility = View.VISIBLE

                tvTotalTicketRejected.visibility = View.GONE
                tvTotalValueRejected.visibility = View.GONE


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
            binding.apply {
                progressAccepted.visibility = View.GONE
                progress.visibility = View.GONE
                progressRejected.visibility = View.VISIBLE

                tvTotalTicket.visibility = View.GONE
                tvTotalValue.visibility = View.GONE

                tvTotalTicketAccepted.visibility = View.GONE
                tvTotalValueAccepted.visibility = View.GONE

                tvTotalTicketRejected.visibility = View.VISIBLE
                tvTotalValueRejected.visibility = View.VISIBLE

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

       // setAdapter(getResponseList())



    }

    private fun setAdapter(getResponseList: List<TicketDataList?>?) {
        adapter = ScanReportTicketNameAdapter(getResponseList)
        binding.rvReportTicketName.adapter = adapter
        binding.rvReportTicketName.setHasFixedSize(true)
    }

}