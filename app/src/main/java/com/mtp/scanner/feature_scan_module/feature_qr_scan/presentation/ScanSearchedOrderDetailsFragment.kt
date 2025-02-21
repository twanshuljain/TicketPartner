package com.mtp.scanner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mtp.scanner.R
import com.mtp.scanner.common.COLUMN
import com.mtp.scanner.common.SCAN_SEARCHED_DATA
import com.mtp.scanner.common.SnackBarUtil
import androidx.lifecycle.Observer
import com.mtp.scanner.common.ZERO
import com.mtp.scanner.databinding.FragmentScanSearchedOrderDetailsBinding
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.Item
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanOrderDetailsUIState
import com.mtp.scanner.utils.DialogProgressUtil
import com.mtp.scanner.utils.NetworkConnectionLiveData
import com.mtp.scanner.utils.Utility.observeOnce


class ScanSearchedOrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentScanSearchedOrderDetailsBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    private var searchDetails: MData? = null
    private var searchDetailsOffline: SearchData? = null
    private val searchDetailsResponse = ArrayList<Item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanSearchedOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkConnectionLiveData = NetworkConnectionLiveData(requireContext())

        viewModel.isOnlineMode.observe(viewLifecycleOwner) {
            if (it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    searchDetails = arguments?.getParcelable(SCAN_SEARCHED_DATA, MData::class.java)
                } else {
                    searchDetails = arguments?.getParcelable<MData>(SCAN_SEARCHED_DATA)
                }

                binding.apply {
                    tvName.text = searchDetails?.name.toString()
                    tvEmail.text = searchDetails?.email.toString()
                    tvOrderId.text =
                        requireContext().getString(R.string.order_id) + COLUMN + searchDetails?.order_id.toString()
                    tvPaymentMethod.text =
                        requireContext().getString(R.string.payment_method) + COLUMN + searchDetails?.payment_type.toString()
                }

                viewModel.isAllChecked.observe(viewLifecycleOwner) { isChecked ->
                    visibleCheckedInButton(isChecked)
                }
                networkConnectionLiveData.observeOnce(
                    viewLifecycleOwner,
                    Observer { isConnected ->
                        if (isConnected) {
                            searchDetails?.order_id?.let { viewModel.getOrderDetailsResponse(it) }
                            observeOrderDetailsData()
                        } else {
                            //SnackBarUtil.showErrorSnackBar(binding.root, getString(R.string.check_network_availability))
                        }
                    })

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    searchDetailsOffline =
                        arguments?.getParcelable(SCAN_SEARCHED_DATA, SearchData::class.java)
                } else {
                    searchDetailsOffline = arguments?.getParcelable<SearchData>(SCAN_SEARCHED_DATA)
                }
                binding.apply {
                    tvName.text = searchDetailsOffline?.name.toString()
                    tvEmail.text = searchDetailsOffline?.email.toString()
                    tvOrderId.text =
                        requireContext().getString(R.string.order_id) + COLUMN + searchDetailsOffline?.order_ticket_id.toString()
                    tvPaymentMethod.text =
                        requireContext().getString(R.string.payment_method) + COLUMN + searchDetailsOffline?.payment_type.toString()
                }
                viewModel.selectedSearchOrderListData.observe(viewLifecycleOwner) { list ->
                    if (list.all { it?.is_checked_in == true }) {
                        visibleCheckedInButton(true)
                    } else {
                        visibleCheckedInButton(false)
                    }
                }
            }
        }

        initView()

        viewModel.selectedSearchedItemEmailAdd.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
        subTitle?.visibility = View.GONE

        binding.btnCheckIn.setOnClickListener {
            viewModel.isOnlineMode.observe(viewLifecycleOwner) {
                if (it) {
                    networkConnectionLiveData.observeOnce(
                        viewLifecycleOwner,
                        Observer { isConnected ->
                            if (isConnected) {
                                val bottomSheet =
                                    searchDetails?.order_id?.let { id ->
                                        CheckInBottomSheetFragment(
                                            binding.root,
                                            id, searchDetailsResponse
                                        )
                                    }
                                bottomSheet?.show(
                                    requireActivity().supportFragmentManager,
                                    bottomSheet.tag
                                )

                            } else {
                                SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.check_network_availability))
                            }
                        })

                } else {
                    searchDetailsResponse.clear()
                    viewModel.selectedSearchOrderListData.observe(viewLifecycleOwner) { list ->
                        for (i in list) {
                            searchDetailsResponse.add(
                                Item(
                                    i.is_checked_in,
                                    i.order_ticket_id,
                                    i.order_number
                                )
                            )
                        }
                    }
                    val bottomSheet =
                        searchDetailsOffline?.order_ticket_id?.let { id ->
                            CheckInBottomSheetFragment(
                                binding.root,
                                id.toString(), searchDetailsResponse
                            )
                        }
                    bottomSheet?.show(requireActivity().supportFragmentManager, bottomSheet.tag)
                }
            }

        }
    }

    private fun visibleCheckedInButton(value: Boolean) {
        if (value) {
            binding.btnCheckIn.apply {
                isEnabled = false
                text = requireContext().getString(R.string.already_checked_in)
            }
        } else {
            binding.btnCheckIn.apply {
                isEnabled = true
                text = requireContext().getString(R.string.check_in)
            }
        }
    }

    private fun observeOrderDetailsData() {
        viewModel.observeScanOrderDetailsData.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanOrderDetailsUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanOrderDetailsUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    if (it.onSuccess.data?.size!! > ZERO) {
                        for (i in ZERO until it.onSuccess.data?.size!!)
                            it.onSuccess.data[i]?.let { it1 -> searchDetailsResponse.add(it1) }
                    }
                }

                is QrScanOrderDetailsUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onFailure)
                }
            }
        }
    }
}


