package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.TP_LOCAL_DATABASE
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.common.storage.PrefConstants
import com.example.ticketpartner.databinding.FragmentLoginScanModuleBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.DataItems
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.EventDetails
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetCheckInDataOfflineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.GetQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertCheckInDataOfflineUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertEventDetailsResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertQrCodeForOffLineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportDataResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertScanReportTicketListUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataOfflineUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypesOfflineScanUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinUIState
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.GetScanSearchDataOfflineUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList
import com.example.ticketpartner.utils.BackPressHandler
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.example.ticketpartner.utils.NetworkConnectionLiveData
import com.example.ticketpartner.utils.Utility
import com.example.ticketpartner.utils.Utility.hideKeyboard
import com.example.ticketpartner.utils.Utility.observeOnce
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginScanModuleFragment : Fragment() {
    private lateinit var binding: FragmentLoginScanModuleBinding
    private val viewModel: LoginScanVewModel by activityViewModels()
    private var etName = EMPTY_STRING
    private var etPin = EMPTY_STRING
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    //arraylist for insert data in local DB
    private val insertTicketTypesList = ArrayList<InsertTicketTypeListResponse>()
    private val insertQrDataList = ArrayList<DataItems>()
    private val insertCheckInDataList = ArrayList<CheckInData>()
    private val insertSearchDataList = ArrayList<SearchData>()
    private val insertScanReportTicketDataList = ArrayList<TicketDataList>()
    private var hasScanReportApiCalled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginScanModuleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleOnBackPressedButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)

        /** restrict user to enter space */
        Utility.disableSpace(binding.etPin)

        binding.etName.doAfterTextChanged {
            etName = it.toString().trim()
        }

        binding.etPin.doAfterTextChanged {
            if (it.toString().length > ZERO) {
                etPin = it.toString().trim()
                binding.ivClearText.visibility = View.VISIBLE
            } else {
                binding.ivClearText.visibility = View.GONE
            }
        }

        binding.ivClearText.setOnClickListener {
            binding.etPin.setText(EMPTY_STRING)
            etPin = EMPTY_STRING
        }

        binding.rlContinue.setOnClickListener {
            if (isAllFieldsValid()) {
                networkConnectionLiveData.observeOnce(viewLifecycleOwner, Observer { isConnected ->
                    if (isConnected) {
                        viewModel.loginWithPin(etName, etPin)
                        observeLoginResponse()
                        hasScanReportApiCalled = false
                    } else {
                        SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.check_network_availability))
                    }
                })
            }
        }

        binding.ivTpLogo.setOnClickListener {
            SnackBarUtil.showCustomSnackBar(binding.root, "Your local data has been clear!")
            requireActivity().deleteDatabase(TP_LOCAL_DATABASE)
        }

    }

    private fun observeLoginResponse() {
        viewModel.observePinLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is LoginWithPinUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is LoginWithPinUIState.OnSuccess -> {
                    /** update user login session */
                    updateUserDetailsToSession(it.onSuccess)
                    /** insert event details data in local DB */
                    insertEventsDetailLocalStorage(it.onSuccess.data?.event)

                    /** insert ticket types list in local DB */
                    val eventTickets = it.onSuccess.data?.event?.event_tickets
                    if (!eventTickets.isNullOrEmpty()) {
                        insertTicketTypesList.clear()
                        for (data in eventTickets) data?.ticket_name?.let { name ->
                            insertTicketTypesList.add(InsertTicketTypeListResponse(name))
                        }
                    }
                    insertTicketTypesInLocalDB(insertTicketTypesList)
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onSuccess.message.toString(),true)
                }

                is LoginWithPinUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun insertEventsDetailLocalStorage(data: EventDetails?) {
        data?.let { event ->
            viewModel.insertEventDetailsForOfflineScan(
                InsertEventDetailsResponse(
                    event.city,
                    event.country,
                    event.door_close_time,
                    event.door_open_time,
                    event.event_cover_image,
                    event.event_end_date,
                    event.event_end_time,
                    event.event_start_date,
                    event.event_start_time,
                    event.id,
                    event.name,
                    event.organization_country_name,
                    event.organization_logo,
                    event.organization_name,
                    event.state
                )
            )
        }
    }

    private fun observeQrCodeListResponseForOfflineScan() {
        viewModel.getQrCodeListForOfflineScan.observe(viewLifecycleOwner) {
            when (it) {
                is GetQrCodeForOffLineScanUIState.IsLoading -> {}

                is GetQrCodeForOffLineScanUIState.OnSuccess -> {
                    insertQrDataList.clear()
                    for (i in ZERO until it.onSuccess.data?.size!!) {
                        it.onSuccess.data[i]?.let { qrData ->
                            insertQrDataList.add(qrData)
                        }
                    }
                    insertQrCodeListDataLocalDB(insertQrDataList)
                }

                is GetQrCodeForOffLineScanUIState.OnFailure -> {
                }
            }
        }
    }


    private fun getCheckInDataOffline() {
        viewModel.getCheckInDataForOfflineScan.observe(viewLifecycleOwner) {
            when (it) {
                is GetCheckInDataOfflineScanUIState.IsLoading -> {}
                is GetCheckInDataOfflineScanUIState.OnSuccess -> {
                    if (!it.onSuccess.data.isNullOrEmpty()) {
                        insertCheckInDataList.clear()
                        for (i in ZERO until it.onSuccess.data.size) {
                            it.onSuccess.data[i]?.let { item ->
                                insertCheckInDataList.add(item)
                            }
                        }
                    }
                    insertCheckInDataLocalDB(insertCheckInDataList)
                }

                is GetCheckInDataOfflineScanUIState.OnFailure -> {}
            }
        }
    }


    private fun observeSearchDataOffline() {
        viewModel.observeScanSearchData.observe(viewLifecycleOwner) {
            when (it) {
                is GetScanSearchDataOfflineUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is GetScanSearchDataOfflineUIState.OnSuccess -> {
                    if (it.onSuccess?.size!! > ZERO) {
                        insertSearchDataList.clear()
                        for (i in ZERO until it.onSuccess?.size!!) {
                            it.onSuccess[i]?.let { item ->
                                insertSearchDataList.add(item)
                            }
                        }
                    }
                    insertSearchDataListLocalDB(insertSearchDataList)
                    hideKeyboard(requireActivity())
                    DialogProgressUtil.dismiss()
                }

                is GetScanSearchDataOfflineUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                }
            }
        }
    }

    private fun observeScanReportAllData() {
        viewModel.getScanReportAllDataLogin.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanReportAllUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanReportAllUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    val resData = it.onSuccess.data
                    resData?.let {
                        viewModel.insertScanReportDataOffline(
                            InsertScanReportDataResponse(
                                ZERO,
                                resData.online,
                                resData.physical,
                                resData.total_scanned,
                                resData.total_accepted,
                                resData.total_rejected,
                                resData.total_tickets,
                            )
                        )
                    }

                    resData?.ticket_data?.let { ticketList ->
                        insertScanReportTicketDataList.clear()
                        for (data in ticketList) {
                            if (data != null) {
                                insertScanReportTicketDataList.add(data)
                            }
                        }
                        insertScanReportTicketListLocalDB(insertScanReportTicketDataList)
                    }
                }

                is QrScanReportAllUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onFailure)
                }
            }
        }
    }

    private fun updateUserDetailsToSession(loginData: LoginWithPinResponse) {
        loginData.let {
            val gson = Gson()
            val userJson = gson.toJson(it)
            MyPreferences.putString(PrefConstants.LOGGED_USER_DETAILS, userJson)
        }
    }

    private fun isAllFieldsValid(): Boolean {
        if (etName.isNullOrEmpty()) {
            SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.please_enter_your_name))
            return false
        }
        if (etPin.isNullOrEmpty()) {
            SnackBarUtil.showCustomSnackBar(binding.root, getString(R.string.please_enter_your_pin))
            return false
        } else return true
    }

    private fun handleOnBackPressedButton() {
        if (requireActivity().supportFragmentManager.backStackEntryCount > 1) {
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            BackPressHandler.onBackPressed(requireActivity())
        }
    }

    private fun insertTicketTypesInLocalDB(insertTicketTypesList: ArrayList<InsertTicketTypeListResponse>) {
        if (insertTicketTypesList.size > ZERO) {
            viewModel.insertTicketTypesOfflineScan(insertTicketTypesList)
            viewModel.observeInsertTicketTypesOfflineScan.observe(viewLifecycleOwner) {
                when (it) {
                    is InsertTicketTypesOfflineScanUIState.IsLoading -> {}
                    is InsertTicketTypesOfflineScanUIState.OnSuccess -> {
                        /** make API call for qr code list and insert data in local DB */
                        viewModel.getQrCodeListForOfflineScan()
                        observeQrCodeListResponseForOfflineScan()
                    }

                    is InsertTicketTypesOfflineScanUIState.OnFailure -> {}
                }
            }
        }
    }

    private fun insertQrCodeListDataLocalDB(insertQrDataList: ArrayList<DataItems>) {
        if (insertQrDataList.size > ZERO) {
            viewModel.insetQrCodeListForOfflineScan(
                insertQrDataList
            )
            viewModel.insertQrCodeListForOfflineScan.observe(viewLifecycleOwner) {
                when (it) {
                    is InsertQrCodeForOffLineScanUIState.IsLoading -> {}
                    is InsertQrCodeForOffLineScanUIState.OnSuccess -> {
                        viewModel.getCheckInDataForOffline()
                        getCheckInDataOffline()
                    }

                    is InsertQrCodeForOffLineScanUIState.OnFailure -> {}
                }
            }
        }
    }

    private fun insertCheckInDataLocalDB(insertCheckInDataList: java.util.ArrayList<CheckInData>) {
        viewModel.insertCheckInDataOfflineScan(
            insertCheckInDataList
        )
        viewModel.insertCheckInDataOfflineScan.observe(viewLifecycleOwner) {
            when (it) {
                is InsertCheckInDataOfflineUIState.IsLoading -> {}
                is InsertCheckInDataOfflineUIState.OnSuccess -> {
                    viewModel.getSearchData()
                    observeSearchDataOffline()
                }

                is InsertCheckInDataOfflineUIState.OnFailure -> {}
            }
        }
    }

    private fun insertSearchDataListLocalDB(insertSearchDataList: java.util.ArrayList<SearchData>) {
        if (insertSearchDataList.size > ZERO) {
            viewModel.insertSearchDataOfflineScan(
                insertSearchDataList
            )
            viewModel.insertSearchDataOfflineScan.observe(viewLifecycleOwner) {
                when (it) {
                    is InsertSearchDataOfflineUIState.IsLoading -> {}
                    is InsertSearchDataOfflineUIState.OnSuccess -> {
                        if (!hasScanReportApiCalled){
                            hasScanReportApiCalled = true
                            viewModel.getScanReportAllData("all")
                        }
                    }

                    is InsertSearchDataOfflineUIState.OnFailure -> {}
                }
            }

            observeScanReportAllData()
        }
    }

    private fun insertScanReportTicketListLocalDB(insertScanReportTicketDataList: java.util.ArrayList<TicketDataList>) {
        if (insertScanReportTicketDataList.size > ZERO) {
            viewModel.insertScanReportTicketListOffline(insertScanReportTicketDataList)
            viewModel.insertScanReportTicketListDataOffline.observe(viewLifecycleOwner) {
                when (it) {
                    is InsertScanReportTicketListUIState.IsLoading -> {}
                    is InsertScanReportTicketListUIState.OnSuccess -> {
                        findNavController().navigateWithClearNavGraph(
                            R.id.main_nav_graph, R.id.eventDetailsScanModuleFragment
                        )
                    }
                    is InsertScanReportTicketListUIState.OnFailure -> {}
                }
            }
        }
    }
}