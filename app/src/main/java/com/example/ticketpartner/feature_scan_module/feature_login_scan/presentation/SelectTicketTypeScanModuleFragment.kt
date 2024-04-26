package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentSelectTicketTypeScanModuleBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState

class SelectTicketTypeScanModuleFragment : Fragment() {
    private lateinit var binding: FragmentSelectTicketTypeScanModuleBinding
    private lateinit var adapter: SelectTicketTypeScanAdapter
    private val viewModel: LoginScanVewModel by activityViewModels()
    private var selectedTicketName = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectTicketTypeScanModuleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeTicketTypeResponse()
    }

    private fun initView() {
        adapter =
            SelectTicketTypeScanAdapter(requireActivity(), emptyList(), ::selectedTicketNameList)
        binding.includeTitle.subTitle.visibility = View.GONE

        binding.includeTitle.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tvSelectAll.setOnClickListener {
            adapter.selectAll()
        }

        binding.btnContinue.setOnClickListener {
        /*    val bundle = bundleOf(SELECT_SCAN_TICKET_ARRAY to selectedTicketName)
            findNavController().navigate(R.id.scanQRLandingFragment, bundle)*/
        }
    }

    private fun observeTicketTypeResponse() {
        viewModel.observeScanEventDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EventDetailsScanUIState.IsLoading -> {}
                is EventDetailsScanUIState.OnSuccess -> {
                    setTicketTypesAdapter(it.onSuccess.data)
                }

                is EventDetailsScanUIState.OnFailure -> {}
            }
        }
    }

    private fun setTicketTypesAdapter(data: DataItem?) {
        adapter = SelectTicketTypeScanAdapter(
            requireActivity(),
            data?.event_tickets,
            ::selectedTicketNameList
        )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)
    }

    private fun selectedTicketNameList(list: ArrayList<String>) {
        if (list.size > ZERO) {
            selectedTicketName = list
        }
    }
}