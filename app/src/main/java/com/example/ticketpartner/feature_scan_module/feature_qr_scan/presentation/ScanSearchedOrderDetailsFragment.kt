package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.COLUMN
import com.example.ticketpartner.common.SCAN_SEARCHED_DATA
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentScanSearchedOrderDetailsBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.Item
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.QrScanOrderDetailsUIState
import com.example.ticketpartner.utils.DialogProgressUtil


class ScanSearchedOrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentScanSearchedOrderDetailsBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private var searchDetails: MData? = null
    private val searDetailsResponse = ArrayList<Item>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanSearchedOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            searchDetails = arguments?.getParcelable(SCAN_SEARCHED_DATA, MData::class.java)
        } else {
            searchDetails = arguments?.getParcelable<MData>(SCAN_SEARCHED_DATA)
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

        binding.apply {
            tvName.text = searchDetails?.name.toString()
            tvEmail.text = searchDetails?.email.toString()
            tvOrderId.text =
                requireContext().getString(R.string.order_id) + COLUMN + searchDetails?.order_id.toString()
            tvPaymentMethod.text =
                requireContext().getString(R.string.payment_method) + COLUMN + searchDetails?.payment_type.toString()
        }
       /* searchDetails?.is_checked_in.let {
            if (it!!) {
                binding.btnCheckIn.apply {
                    isEnabled = false
                    text = requireContext().getString(R.string.checked)
                }
            } else {
                binding.btnCheckIn.apply {
                    isEnabled = true
                    text = requireContext().getString(R.string.check_in)
                }
            }
        }*/

        searchDetails?.order_id?.let { viewModel.getOrderDetailsResponse(it) }
        observeOrderDetailsData()

        binding.btnCheckIn.setOnClickListener {
            val bottomSheet =
                searchDetails?.order_id?.let { id ->
                    CheckInBottomSheetFragment(
                        binding.root,
                        id, searDetailsResponse
                    )
                }
            bottomSheet?.show(requireActivity().supportFragmentManager, bottomSheet.tag)
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
                            it.onSuccess.data[i]?.let { it1 -> searDetailsResponse.add(it1) }
                    }
                }

                is QrScanOrderDetailsUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }
}


