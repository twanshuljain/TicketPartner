package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.R
import com.mtp.ticketpartner.scanner.common.EMPTY_STRING
import com.mtp.ticketpartner.scanner.common.SCAN_SEARCHED_DATA
import com.mtp.ticketpartner.scanner.common.ZERO
import com.mtp.ticketpartner.scanner.common.remote.apis.UNAUTHORIZED_USER
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.scanner.databinding.FragmentScanBottomNavSearchBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.QrScanSearchItemUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanSearchDataOfflineUIState
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.mtp.ticketpartner.scanner.utils.DialogProgressUtil
import com.mtp.ticketpartner.scanner.utils.DialogUtils
import com.mtp.ticketpartner.scanner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.mtp.ticketpartner.scanner.utils.NetworkConnectionLiveData
import com.mtp.ticketpartner.scanner.utils.Utility
import com.mtp.ticketpartner.scanner.utils.Utility.filterAndSortOrderList

class ScanBottomNavSearchFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavSearchBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: ScanSearchOrderAdapter
    private lateinit var adapterOffline: ScanSearchOrderOfflineAdapter
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    private var searchOfflineDataList = ArrayList<SearchData>()
    private var searchedItem = EMPTY_STRING
    private var sortedFilteredList = emptyList<SearchData>()
    private var orderNumberList = ArrayList<SearchData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.getScanSearchDataOfflineScan()

        /* viewModel.getScanSearchDataFromLocalDB.observe(viewLifecycleOwner) {
             when (it) {
                 is GetScanSearchDataOfflineUIState.IsLoading -> {}
                 is GetScanSearchDataOfflineUIState.OnSuccess -> {
                     for (i in it.onSuccess) {
                         searchOfflineDataList.add(i)
                     }
                 }

                 is GetScanSearchDataOfflineUIState.OnFailure -> {}
             }
         }*/

        /*  sortedFilteredList =
              filterAndSortOrderList(searchOfflineDataList, binding.etSearch.text.toString())
          sortedFilteredList?.let { list ->
              setAdapterOffline(list)
          }*/

    }

    private fun initView() {
        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())

        viewModel.eventName.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
        }

        viewModel.dateTimeEventDetails.observe(viewLifecycleOwner) {
            val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
            subTitle?.visibility = View.VISIBLE
            subTitle?.text = it
        }


        binding.etSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.rvSearchOrder.visibility = View.VISIBLE
                binding.etSearchLayout.setBackgroundResource(R.drawable.edit_text_design_search_bar_puple)
                binding.icClear.visibility = View.VISIBLE
                //observeSearchItemResponse(it.toString())
            } else {
                binding.icClear.visibility = View.GONE
                binding.etSearchLayout.setBackgroundResource(R.drawable.edit_text_design_search_bar)
                setAdapter(ArrayList())
                binding.rvSearchOrder.visibility = View.GONE
                binding.rvSearchOrderOffline.visibility = View.GONE
                binding.tvNoRecordFound.visibility = View.VISIBLE
                binding.tvNoRecordFound.text = getString(R.string.search_here)
            }
        }

        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                observeSearchItemResponse(binding.etSearch.text.toString())
                true
            } else {
                false
            }
        }
        binding.icClear.setOnClickListener {
            binding.etSearch.text?.clear()
        }
    }

    private fun observeSearchItemResponse(orderId: String) {
        setAdapterOffline(emptyList<SearchData>())
        searchedItem = orderId
        searchOfflineDataList.clear()
        /** Observe network connection status only once */
        viewModel.isNetworkAvailableObserver.observe(viewLifecycleOwner){res ->
            if (res){
                binding.rvSearchOrder.visibility = View.VISIBLE
                binding.rvSearchOrderOffline.visibility = View.GONE

                viewModel.getSearchData(orderId)

                viewModel.observeScanSearchData.observe(viewLifecycleOwner) {
                    when (it) {
                        is QrScanSearchItemUIState.IsLoading -> {
                            DialogProgressUtil.show(childFragmentManager)
                        }

                        is QrScanSearchItemUIState.OnSuccess -> {
                            DialogProgressUtil.dismiss()
                            it.onSuccess.data?.let { list ->
                              //  val filteredList = list.filter { it?.ticket_name in selectedTicketTypeList }

                                if (list.isNotEmpty()){
                                    binding.tvNoRecordFound.visibility = View.GONE
                                    setAdapter(list)
                                } else {
                                    binding.tvNoRecordFound.visibility = View.VISIBLE
                                    binding.tvNoRecordFound.text = getString(R.string.no_data_found)
                                    binding.rvSearchOrder.visibility = View.GONE
                                }

                            }
                        }

                        is QrScanSearchItemUIState.OnFailure -> {
                            DialogProgressUtil.dismiss()
                            if (it.onFailure == UNAUTHORIZED_USER.toString()){
                                expireSession()
                            }
                        }
                    }
                }
            }else{

                binding.rvSearchOrder.visibility = View.GONE
                binding.rvSearchOrderOffline.visibility = View.VISIBLE

                viewModel.getScanSearchDataFromLocalDB.observe(viewLifecycleOwner) {
                    when (it) {
                        is GetScanSearchDataOfflineUIState.IsLoading -> {}
                        is GetScanSearchDataOfflineUIState.OnSuccess -> {
                            it.onSuccess?.let { data ->
                                for (i in data!!) {
                                    if (i != null) {
                                        searchOfflineDataList.add(i)
                                    }
                                }
                            }
                        }

                        is GetScanSearchDataOfflineUIState.OnFailure -> {}
                    }
                }
            }
        }

        sortedFilteredList = filterAndSortOrderList(searchOfflineDataList, orderId)
        sortedFilteredList?.let { list ->
            setAdapterOffline(list)
        }
    }

    private fun setAdapterOffline(data: List<SearchData>) {
        data?.let {
            adapterOffline =
                ScanSearchOrderOfflineAdapter(requireActivity(), it, ::isItemClickedOffline)
            binding.rvSearchOrderOffline.adapter = adapterOffline
            binding.rvSearchOrderOffline.setHasFixedSize(true)
            adapterOffline.notifyDataSetChanged()
        }
    }

    private fun setAdapter(data: List<MData?>) {
        data?.let {
            adapter = ScanSearchOrderAdapter(requireActivity(), it, ::isItemClicked)
            binding.rvSearchOrder.adapter = adapter
            binding.rvSearchOrder.setHasFixedSize(true)
            adapter.notifyDataSetChanged()
        }
    }

    private fun isItemClicked(data: MData) {
        viewModel.isAllChecked.value = data.is_checked_in
        viewModel.isOnlineMode.value = true
        viewModel.selectedSearchedItemEmailAdd.value = data.email.toString()
        val bundle = Bundle()
        bundle.putParcelable(SCAN_SEARCHED_DATA, data)
        findNavController().navigate(
            R.id.action_scanBottomNavSearchFragment_to_scanSearchedOrderDetailsFragment,
            bundle
        )
    }

    private fun isItemClickedOffline(data: SearchData) {
        orderNumberList.clear()
        data.order_number?.let {
            for (i in ZERO until searchOfflineDataList.size) {
                if (it == searchOfflineDataList[i].order_number) {
                    orderNumberList.add(searchOfflineDataList[i])
                }
            }
        }
        viewModel.putSelectedOrderList(orderNumberList)
        viewModel.isOnlineMode.value = false
        viewModel.selectedSearchedItemEmailAdd.value = data.email.toString()
        val bundle = Bundle()
        bundle.putParcelable(SCAN_SEARCHED_DATA, data)
        findNavController().navigate(
            R.id.action_scanBottomNavSearchFragment_to_scanSearchedOrderDetailsFragment,
            bundle
        )
    }

    override fun onResume() {
        super.onResume()
        if (binding.etSearch.text.toString().isNotEmpty()) {
            searchOfflineDataList.clear()
            viewModel.getScanSearchDataOfflineScan()
            viewModel.getScanSearchDataFromLocalDB.observe(viewLifecycleOwner) {
                when (it) {
                    is GetScanSearchDataOfflineUIState.IsLoading -> {}
                    is GetScanSearchDataOfflineUIState.OnSuccess -> {
                        it.onSuccess?.let { data ->
                            for (i in data!!) {
                                if (i != null) {
                                    searchOfflineDataList.add(i)
                                }
                            }

                        }
                        sortedFilteredList = filterAndSortOrderList(
                            searchOfflineDataList,
                            binding.etSearch.text.toString()
                        )
                        sortedFilteredList?.let { list ->
                            val uniqueList = list.toSet()
                            val uniqueArrayList = ArrayList(uniqueList)
                            setAdapterOffline(uniqueArrayList)
                        }
                    }
                    is GetScanSearchDataOfflineUIState.OnFailure -> {}
                }
            }
        }
    }
    private fun expireSession() {
        val title = requireContext().getString(R.string.session_expired)
        val message = requireContext().getString(R.string.your_session_has_been_expired)
        val builder =   DialogUtils.customAlertDialog(requireContext(),title, message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            Utility.clearLocalDatabase(requireActivity())
            MyPreferences.clearpref()
            findNavController().navigateWithClearNavGraph(
                R.id.nested_qr_scan_nav_graph,
                R.id.loginScanModuleFragment
            )
        }
        val mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }
}




