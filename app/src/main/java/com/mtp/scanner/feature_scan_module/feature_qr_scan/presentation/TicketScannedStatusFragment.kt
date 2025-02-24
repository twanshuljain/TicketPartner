package com.mtp.scanner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.R
import com.mtp.scanner.databinding.FragmentTicketScannedStatusBinding
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.mtp.scanner.utils.TimePickerUtility

class TicketScannedStatusFragment : Fragment() {
    private lateinit var binding: FragmentTicketScannedStatusBinding
    private val viewModel: QrScanViewModel by activityViewModels()
    private var currentTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketScannedStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentTime = TimePickerUtility.getCurrentTimeWithAmPm()
        viewModel.eventName.observe(viewLifecycleOwner) { binding.tvEventName.text = it.toString()}
        binding.tvCurrentTime.text = currentTime
        binding.llRootLayout.setOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.observeQrScanResponse.observe(viewLifecycleOwner) {
            when (it) {
                is QrScanUIState.IsLoading -> {}
                is QrScanUIState.OnSuccess -> {
                    binding.llRootLayout.background =
                        requireContext().getDrawable(R.drawable.green_corner_curve_layout)
                    binding.tvTicketStatusMessage.text = it.onSuccess.message.toString()
                    binding.tvName.text = it.onSuccess.data?.customer_name
                    binding.ivStatusIcon.setImageResource(R.drawable.ic_currect_circle_white_49)
                }

                is QrScanUIState.OnFailure -> {
                    binding.ivStatusIcon.setImageResource(R.drawable.ic_white_invalid)

                    if (it.isTransfer){
                        binding.tvTicketStatusMessage.text = requireContext().getString(R.string.ticket_transferred_error)
                    } else if (it.isRefunded){
                        binding.tvTicketStatusMessage.text = requireContext().getString(R.string.ticket_refunded_error)
                    } else {
                        binding.tvTicketStatusMessage.text = it.onFailure.toString()
                    }

                    binding.tvName.text = it.customerName

                    binding.llRootLayout.background =
                        requireContext().getDrawable(R.drawable.red_corner_curve_layout)
                }
            }
        }
    }
}