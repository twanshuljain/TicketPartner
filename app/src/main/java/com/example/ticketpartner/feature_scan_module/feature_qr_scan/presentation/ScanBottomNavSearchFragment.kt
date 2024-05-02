package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentScanBottomNavSearchBinding
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.QrScanSearchItemUIState
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.MData
import com.example.ticketpartner.utils.DialogProgressUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScanBottomNavSearchFragment : Fragment() {
    private lateinit var binding: FragmentScanBottomNavSearchBinding
    private lateinit var adapter: ScanSearchOrderAdapter
    private val viewModel: QrScanViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBottomNavSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        binding.etSearch.addTextChangedListener {
            if (it.toString().length > ZERO){
                binding.rvSearchOrder.visibility = View.VISIBLE
                binding.etSearchLayout.setBackgroundResource(R.drawable.edit_text_design_search_bar_puple)
                binding.icClear.visibility = View.VISIBLE
                observeSearchItemResponse(it.toString())
            }else{
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
        viewModel.observeScanSearchData.observe(viewLifecycleOwner){
            when(it){
                is QrScanSearchItemUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is QrScanSearchItemUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.onSuccess.data?.let {list ->
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
            adapter = ScanSearchOrderAdapter(requireActivity(),it, ::isItemClicked)
            binding.rvSearchOrder.adapter = adapter
            binding.rvSearchOrder.setHasFixedSize(true)
            binding.rvSearchOrder.adapter = adapter
        }
    }


        private fun openImagePickerBottomSheet() {
            val dialog = BottomSheetDialog(requireContext())
            val dialogView = LayoutEndScanBottomDialogBinding.inflate(layoutInflater)
            dialogView.apply {
                tvTitle.text = getString(R.string.end_scan_with_mark)
                tvDescription.text = getString(R.string.are_you_sure_end_scan)
            }
            dialogView.btnNo.setOnClickListener {
                dialog.dismiss()
            }
            dialogView.btnYes.setOnClickListener {
                findNavController().navigate(R.id.qrScanReportFragment)
                dialog.dismiss()
            }
            dialogView.ivClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(dialogView.root)
            dialog.show()
        }

    private fun isItemClicked(orderId: String) {
            val bottomSheet = CheckInBottomSheetFragment(orderId)
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)


          /*  //openImagePickerBottomSheet()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            transaction.replace(R.id.frameLayout, ScanSearchedOrderDetailsFragment())
            transaction.addToBackStack(null)
            transaction.commit()*/
           // findNavController().navigate(R.id.scanSearchedOrderDetailsFragment)

    }
}




