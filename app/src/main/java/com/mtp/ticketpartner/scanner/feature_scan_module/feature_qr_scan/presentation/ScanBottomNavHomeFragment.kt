package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.VERTICAL_POLE
import com.mtp.ticketpartner.scanner.common.ZERO
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.ticketpartner.scanner.common.storage.PrefConstants
import com.mtp.ticketpartner.scanner.common.storage.PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST
import com.mtp.ticketpartner.scanner.databinding.FragmentScanBottomNavHomeBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.presentation.SelectTicketTypeScanAdapter
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.GetTicketTypesListOfflineUIState
import com.mtp.ticketpartner.scanner.utils.getFormattedStartDateForEvent
import com.mtp.ticketpartner.scanner.utils.getFormattedTimeForEvent

class ScanBottomNavHomeFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavHomeBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: SelectTicketTypeScanAdapter
    private val ticketTypesList = ArrayList<InsertTicketTypeListResponse>()
    private var _ticketTypesList = ArrayList<InsertTicketTypeListResponse>()
    var isSelected = false
    var allSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavHomeBinding.inflate(layoutInflater)
        ticketTypesList.clear()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.isEnabled = false
        setDetailsOnCard()

        viewModel.getTicketTypesListFromLocal()
        observeTicketTypesListFromLocalDB()

        initView()
        viewModel.selectedTicketTypeArrayList.value = null
        viewModel.listSize = ZERO
    }

    private fun isContinueButtonEnable(value: Boolean) {
        if (value) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.background =
                requireContext().getDrawable(R.drawable.btn_design_dark_primary)
        } else {
            binding.btnContinue.isEnabled = false
            binding.btnContinue.background =
                requireContext().getDrawable(R.drawable.disable_continue_btn_design)
        }
    }

    private fun selectedListSize(size: Int) {}

    private fun setDetailsOnCard() {
        val userLoginDetails = MyPreferences.getUserDetails()
        viewModel.eventName.value = userLoginDetails?.data?.event?.name
        binding.tvTitle.text = userLoginDetails?.data?.event?.name
        /*val startDate =
            getFormattedStartDateForEvent(userLoginDetails?.data?.event?.event_start_date) + VERTICAL_POLE

        val startEndTime =
            getFormattedTimeForEvent(userLoginDetails?.data?.event?.event_start_time) + HYPHEN_CHAR + getFormattedTimeForEvent(
                userLoginDetails?.data?.event?.event_end_time
            )*/
        val startDate =
            getFormattedStartDateForEvent(userLoginDetails?.data?.event?.event_start_date)
        val startTime = getFormattedTimeForEvent(userLoginDetails?.data?.event?.event_start_time)
        val endDate = getFormattedStartDateForEvent(userLoginDetails?.data?.event?.event_end_date)
        val endTime = getFormattedTimeForEvent(userLoginDetails?.data?.event?.event_end_time)

        viewModel.dateTimeEventDetails.value =
            startDate + VERTICAL_POLE + startTime

       // viewModel.dateTimeEventDetails.value = startDate + startEndTime
    }


    private fun initView() {
        adapter =
            SelectTicketTypeScanAdapter(
                requireActivity(),
                ticketTypesList,
                ::selectedTicketNameList, ::selectedListSize
            )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)

        val selectedTicketTypeListSize =
            MyPreferences.getArrayList(PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST)
        if (selectedTicketTypeListSize.size > ZERO) {
            isContinueButtonEnable(true)
        }

        val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
        subTitle?.visibility = View.GONE

        viewModel.eventName.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = getString(R.string.select_ticket)
        }

        isSelected = allSelected
        binding.llSelectAll.setOnClickListener {
            if (isSelected) {
                isSelected = false
                adapter.unselectAll()
                binding.tvSelectAll.text = getString(R.string.select_all)
                binding.ivSelectAllChecked.visibility = View.GONE
                binding.ivSelectAllUnChecked.visibility = View.VISIBLE
            } else {
                isSelected = true
                adapter.selectAll()
                binding.tvSelectAll.text = getString(R.string.unselect_all)
                binding.ivSelectAllChecked.visibility = View.VISIBLE
                binding.ivSelectAllUnChecked.visibility = View.GONE
            }
        }

        binding.rvSelectTicketType.setHasFixedSize(true)

        binding.btnContinue.setOnClickListener {
            viewModel.onContinueClick.value = R.id.scanBottomQRScanFragment
        }
        viewModel.getQrCodeListFromLocalDB()
        viewModel.getCheckInDataFromLocalDB()
    }

    private fun observeTicketTypesListFromLocalDB() {
        viewModel.getTicketTypesListFromLocalDB.observe(viewLifecycleOwner) {
            when (it) {
                is GetTicketTypesListOfflineUIState.IsLoading -> {}
                is GetTicketTypesListOfflineUIState.OnSuccess -> {
                    val savedSelectedList =
                        MyPreferences.getArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)
                    ticketTypesList.clear()
                    for (data in it.onSuccess)
                        ticketTypesList.add(
                            (InsertTicketTypeListResponse(
                                data.ticketName.toString(),
                                data.isSelected
                            ))
                        )
                    for (i in ZERO until ticketTypesList.size) {
                        if (savedSelectedList.contains(ticketTypesList[i].ticketName)) {
                            ticketTypesList[i].isSelected = true
                        }
                    }

                    allSelected = ticketTypesList.all { it.isSelected == true }
                    if (allSelected){
                        binding.tvSelectAll.text = getString(R.string.unselect_all)
                        binding.ivSelectAllChecked.visibility = View.VISIBLE
                        binding.ivSelectAllUnChecked.visibility = View.GONE
                    } else{
                        binding.tvSelectAll.text = getString(R.string.select_all)
                        binding.ivSelectAllChecked.visibility = View.GONE
                        binding.ivSelectAllUnChecked.visibility = View.VISIBLE
                    }
                    setTicketTypesAdapter(ticketTypesList)
                    initView()
                }

                is GetTicketTypesListOfflineUIState.OnFailure -> {}
            }
        }
    }

    private fun setTicketTypesAdapter(data: ArrayList<InsertTicketTypeListResponse>?) {
        _ticketTypesList = data ?: ArrayList()
        adapter = SelectTicketTypeScanAdapter(
            requireActivity(),
            data,
            ::selectedTicketNameList, ::selectedListSize
        )
        binding.rvSelectTicketType.adapter = adapter
        binding.rvSelectTicketType.setHasFixedSize(true)
    }

    private fun selectedTicketNameList(list: ArrayList<String>) {
        viewModel.selectedTicketTypeArrayList.value = list

        viewModel.listSize = list.size
        MyPreferences.putArrayList(SCAN_SELECTED_TICKET_TYPES_LIST, list)

        // Convert List<String> to List<InsertTicketTypeListResponse>
        _ticketTypesList.forEach { item ->
            item.isSelected = list.contains(item.ticketName)
        }

        viewModel.insertTicketTypesOfflineScan(_ticketTypesList)

        val selectedTicketTypeListSize =
            MyPreferences.getArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)

        if (selectedTicketTypeListSize.size > ZERO) {
            isContinueButtonEnable(true)
           // binding.tvSelectAll.text = getString(R.string.unselect_all)
            binding.ivSelectAllChecked.visibility = View.VISIBLE
            binding.ivSelectAllUnChecked.visibility = View.GONE
        } else {
            MyPreferences.clearArrayList(SCAN_SELECTED_TICKET_TYPES_LIST)
            binding.tvSelectAll.text = getString(R.string.select_all)
            binding.ivSelectAllChecked.visibility = View.GONE
            binding.ivSelectAllUnChecked.visibility = View.VISIBLE

            isContinueButtonEnable(false)
        }

    }
}