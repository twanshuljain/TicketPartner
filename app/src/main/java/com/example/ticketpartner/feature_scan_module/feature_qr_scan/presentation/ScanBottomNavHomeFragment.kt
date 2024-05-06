package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.COMMA
import com.example.ticketpartner.common.HYPHEN_CHAR
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.VERTICAL_POLE
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentScanBottomNavHomeBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItem
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation.SelectTicketTypeScanAdapter
import com.example.ticketpartner.utils.BackPressHandler
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.getFormattedStartDateForEvent
import com.example.ticketpartner.utils.getFormattedTimeForEvent

class ScanBottomNavHomeFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavHomeBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: SelectTicketTypeScanAdapter

    private var tempList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        makeEventDetailsAPICall()
        viewModel.selectedTicketTypeArrayList.value = null
        binding.btnContinue.isEnabled = false
        viewModel.listSize = 0
    }

    private fun makeEventDetailsAPICall() {
        adapter =
            SelectTicketTypeScanAdapter(
                requireActivity(),
                emptyList(),
                ::selectedTicketNameList, ::selectedListSize
            )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)

        viewModel.getEventDetailsHomeData()
        viewModel.observeScanEventDetailsHomeResponse.observe(viewLifecycleOwner) {
            when (it) {
                is EventDetailsScanUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is EventDetailsScanUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()

                    /*   for (i in 0 until it.onSuccess?.data?.event_tickets?.size!!) {
                           val person = it.onSuccess.data.event_tickets[i]
                           viewModel.selectedTicketTypeArrayList.observe(viewLifecycleOwner) { data ->
                               if (person?.ticket_name in data) {
                                   person?.isSelected = true
                                   binding.btnContinue.background =
                                       requireContext().getDrawable(R.drawable.btn_design_dark_primary)
                               }
                           }
                       }*/

                    setTicketTypesAdapter(it.onSuccess.data)
                    setDetailsOnCard(it.onSuccess.data)
                }

                is EventDetailsScanUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun selectedListSize(size: Int) {
        /*   if (size > 0) {
               binding.btnContinue.background =
                   requireContext().getDrawable(R.drawable.btn_design_dark_primary)
           } else {
               binding.btnContinue.background =
                   requireContext().getDrawable(R.drawable.disable_continue_btn_design)
           }*/
    }

    private fun setDetailsOnCard(data: DataItem?) {
        viewModel.eventName.value = data?.event?.name
        binding.tvTitle.text = data?.event?.name
        val startDate =
            getFormattedStartDateForEvent(data?.event_dates?.event_start_date) + VERTICAL_POLE
        binding.tvStartDate.text = startDate

        val startEndTime =
            getFormattedTimeForEvent(data?.event_dates?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                data?.event_dates?.event_end_time
            )
        binding.tvStartEndTime.text = startEndTime
        val location = data?.event_locations
        binding.tvLocation.text =
            location?.city + COMMA + location?.state + COMMA + location?.country

        viewModel.dateTimeEventDetails.value = startDate + startEndTime
    }


    private fun initView() {
        val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
        subTitle?.visibility = View.GONE

        viewModel.eventName.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = getString(R.string.select_ticket_type)
        }

        binding.tvSelectAll.setOnClickListener {
            adapter.selectAll()
        }
        binding.rvSelectTicketType.setHasFixedSize(true)

        binding.btnContinue.setOnClickListener {
            viewModel.onContinueClick.value = R.id.scanBottomQRScanFragment
        }
    }

    private fun setTicketTypesAdapter(data: DataItem?) {
        adapter = SelectTicketTypeScanAdapter(
            requireActivity(),
            data?.event_tickets,
            ::selectedTicketNameList, ::selectedListSize
        )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)
    }

    private fun selectedTicketNameList(list: ArrayList<String>) {
        viewModel.selectedTicketTypeArrayList.value = list
        viewModel.listSize = list.size
        if (list.size > ZERO) {
            //  viewModel.selectedTicketTypeArrayList.value = list
            // viewModel.putSelectedTicketName(list)
            binding.btnContinue.isEnabled = true
            binding.btnContinue.background =
                requireContext().getDrawable(R.drawable.btn_design_dark_primary)
        } else {
            binding.btnContinue.isEnabled = false
            /// viewModel.putSelectedTicketName(list)
            //     viewModel.selectedTicketTypeArrayList.value = list
            binding.btnContinue.background =
                requireContext().getDrawable(R.drawable.disable_continue_btn_design)
        }

        //    viewModel.putSelectedTicketName(list)

    }
}