package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SCAN_SEARCHED_DATA
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentScanBottomNavSearchBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.QrScanSearchItemUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.example.ticketpartner.utils.DialogProgressUtil

class ScanBottomNavSearchFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavSearchBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private lateinit var adapter: ScanSearchOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBottomNavSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
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
            if (it.toString().length > ZERO) {
                binding.rvSearchOrder.visibility = View.VISIBLE
                binding.etSearchLayout.setBackgroundResource(R.drawable.edit_text_design_search_bar_puple)
                binding.icClear.visibility = View.VISIBLE
                observeSearchItemResponse(it.toString())
            } else {
                binding.icClear.visibility = View.GONE
                binding.etSearchLayout.setBackgroundResource(R.drawable.edit_text_design_search_bar)
                binding.rvSearchOrder.visibility = View.GONE
            }
        }
        binding.icClear.setOnClickListener {
            binding.etSearch.text?.clear()
        }
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

    private fun setAdapter(data: List<MData?>) {
        data?.let {
            adapter = ScanSearchOrderAdapter(requireActivity(), it, ::isItemClicked)
            binding.rvSearchOrder.adapter = adapter
            binding.rvSearchOrder.setHasFixedSize(true)
        }
    }

    private fun isItemClicked(data: MData) {
        viewModel.selectedSearchedItemEmailAdd.value = data.email.toString()
        val bundle = Bundle()
        bundle.putParcelable(SCAN_SEARCHED_DATA, data)
        findNavController().navigate(
            R.id.action_scanBottomNavSearchFragment_to_scanSearchedOrderDetailsFragment,
            bundle
        )
    }
}




