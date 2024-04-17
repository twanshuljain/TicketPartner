package com.example.ticketpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ticketpartner.databinding.FragmentQrScanReportBinding
import com.example.ticketpartner.scan_module.feature_qr_scan.domain.model.TicketData
import com.example.ticketpartner.scan_module.feature_qr_scan.presentation.ScanReportTicketNameAdapter

class QrScanReportFragment : Fragment() {
    private lateinit var binding: FragmentQrScanReportBinding
    private lateinit var adapter: ScanReportTicketNameAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQrScanReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        adapter = ScanReportTicketNameAdapter(emptyList())
        binding.includeTitle.ivBack.visibility = View.GONE

        binding.btnAll.setOnClickListener {
            binding.apply {
                btnAll.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAll.setTextColor(requireContext().getColor(R.color.white))
                btnAccepted.setTextColor(requireContext().getColor(R.color.orange))
                btnRejected.setTextColor(requireContext().getColor(R.color.orange))
            }
        }

        binding.btnAccepted.setOnClickListener {
            binding.apply {
                btnAll.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAll.setTextColor(requireContext().getColor(R.color.orange))
                btnAccepted.setTextColor(requireContext().getColor(R.color.white))
                btnRejected.setTextColor(requireContext().getColor(R.color.orange))
            }
        }
        binding.btnRejected.setOnClickListener {
            binding.apply {
                btnAll.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnAccepted.background =
                    requireContext().getDrawable(R.drawable.orange_border_button_design)
                btnRejected.background =
                    requireContext().getDrawable(R.drawable.curve_btn_black_back_design)
                btnAll.setTextColor(requireContext().getColor(R.color.orange))
                btnAccepted.setTextColor(requireContext().getColor(R.color.orange))
                btnRejected.setTextColor(requireContext().getColor(R.color.white))
            }
        }

        setAdapter(getResponseList())


        val progressValues = listOf(
            150 to requireContext().getColor(R.color.orange),
            250 to requireContext().getColor(R.color.green),
            100 to requireContext().getColor(R.color.blue)
        )
        binding.progress.setProgressValues(progressValues, 500)
    }

    private fun getResponseList(): ArrayList<TicketData> {
        val list = ArrayList<TicketData>()
        for (i in 0..5) {
            list.add(TicketData(2.0, "General Admission", "free", 99, 100))
        }
        return list
    }

    private fun setAdapter(getResponseList: ArrayList<TicketData>) {
        adapter = ScanReportTicketNameAdapter(getResponseList)
        binding.rvReportTicketName.adapter = adapter
        binding.rvReportTicketName.setHasFixedSize(true)
    }

}