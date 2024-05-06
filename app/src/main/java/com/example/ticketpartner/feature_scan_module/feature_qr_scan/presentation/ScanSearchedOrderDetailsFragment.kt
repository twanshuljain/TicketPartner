package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentScanSearchedOrderDetailsBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.QrScanSearchItemUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.example.ticketpartner.utils.DialogProgressUtil

class ScanSearchedOrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentScanSearchedOrderDetailsBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: ScanSearchedOrderDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanSearchedOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.selectedSearchedItemEmailAdd.observe(viewLifecycleOwner) {
            val title = activity?.findViewById<AppCompatTextView>(R.id.title)
            title?.text = it
            observeSearchItemResponse(it)
        }
    }

    private fun initView() {
        val subTitle = activity?.findViewById<AppCompatTextView>(R.id.subTitle)
        subTitle?.visibility = View.GONE

    }

    private fun observeSearchItemResponse(orderId: String) {
        viewModel.getSearchData(orderId)
        viewModel.observeScanSearchData.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanSearchItemUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is QrScanSearchItemUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.onSuccess.data?.let { list ->
                        setAdapter(list)
                    }
                }

                is QrScanSearchItemUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setAdapter(list: List<MData?>) {
        list?.let {
            adapter = ScanSearchedOrderDetailsAdapter(requireActivity(), it, ::isItemClicked)
            binding.rvSearchOrderDetails.adapter = adapter
            binding.rvSearchOrderDetails.setHasFixedSize(true)
            binding.rvSearchOrderDetails.adapter = adapter
        }
    }

    private fun isItemClicked(orderId: String) {
        val bottomSheet = CheckInBottomSheetFragment(binding.root,orderId)
        bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
    }
}


