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
import com.example.ticketpartner.databinding.FragmentScanBottomNavHomeBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventTicket
import com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation.SelectTicketTypeScanAdapter
import com.example.ticketpartner.utils.DialogProgressUtil

class ScanBottomNavHomeFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavHomeBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: SelectTicketTypeScanAdapter

    private var selectedTicketName = ArrayList<String>()
    private var eventTicketsList = ArrayList<EventTicket>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBottomNavHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

      /*  viewModel.observerEventTicketList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                eventTicketsList.clear()
                for (data in it) {
                    data?.let {
                        eventTicketsList.add(data)
                    }
                }
            }
        }*/

        makeEventDetailsAPICall()
        setTicketTypesAdapter(eventTicketsList)

      /*  if (!eventTicketsList.isNullOrEmpty()) {
            setTicketTypesAdapter(eventTicketsList)
        } else {
            makeEventDetailsAPICall()
            setTicketTypesAdapter(eventTicketsList)
        }*/


    }

    private fun makeEventDetailsAPICall() {
        viewModel.getEventDetailsData()
        viewModel.observeScanEventDetailsResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EventDetailsScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EventDetailsScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    setTicketTypesAdapter(it.onSuccess.data?.event_tickets)
                }

                is EventDetailsScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun initView() {
        adapter =
            SelectTicketTypeScanAdapter(requireActivity(), emptyList(), ::selectedTicketNameList)
        binding.tvSelectAll.setOnClickListener {
            adapter.selectAll()
        }

        binding.includeTitle.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        binding.rvSelectTicketType.setHasFixedSize(true)

        binding.btnContinue.setOnClickListener {
            /*  val bundle = bundleOf(SELECTED_TICKET_NAME to selectedTicketName)
              findNavController().navigate(R.id.scanBottomQRScanFragment, bundle)*/
        }

        binding.btnContinue.setOnClickListener {
            viewModel.onContinueClick.value = R.id.scanBottomNavQR
        }

        binding.includeTitle.ivLogOut.setOnClickListener {
           // logoutDialog()
            viewModel.isLogOut.value = true
        }
    }

    private fun setTicketTypesAdapter(data: List<EventTicket?>?) {
        adapter = SelectTicketTypeScanAdapter(
            requireActivity(),
            data,
            ::selectedTicketNameList
        )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)
    }

    private fun selectedTicketNameList(list: ArrayList<String>) {
        if (list.size > ZERO) {
            viewModel.selectedEventName.value = list
        }
    }
}