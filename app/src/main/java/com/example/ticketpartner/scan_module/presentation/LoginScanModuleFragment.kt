package com.example.ticketpartner.scan_module.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.FragmentLoginScanModuleBinding
import com.example.ticketpartner.scan_module.domain.model.LoginWithPinUIState
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScanModuleFragment : Fragment() {
    private lateinit var binding: FragmentLoginScanModuleBinding
    private val viewModel: LoginScanVewModel by activityViewModels()
    private var etName = EMPTY_STRING
    private var etPin = EMPTY_STRING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginScanModuleBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** restrict user to enter space */
        Utility.disableSpace(binding.etName)
        Utility.disableSpace(binding.etPin)

        /** allow char only */
        Utility.allowCharactersOnly(binding.etName)

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
            findNavController().navigate(R.id.eventDetailsScanModuleFragment)
           /* if (isAllFieldsValid()) {
                viewModel.loginWithPin(etName, etPin)
                observeLoginResponse()
            }*/
        }
    }

    private fun observeLoginResponse() {
        viewModel.observePinLoginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is LoginWithPinUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is LoginWithPinUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.onSuccess.message.toString())
                }

                is LoginWithPinUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun isAllFieldsValid(): Boolean {
        if (etName.isNullOrEmpty()) {
            SnackBarUtil.showErrorSnackBar(binding.root, getString(R.string.please_enter_your_name))
            return false
        }
        if (etPin.isNullOrEmpty()) {
            SnackBarUtil.showErrorSnackBar(binding.root, getString(R.string.please_enter_your_pin))
            return false
        } else return true
    }
}