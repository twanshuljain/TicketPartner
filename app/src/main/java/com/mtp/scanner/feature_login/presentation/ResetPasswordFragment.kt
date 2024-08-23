package com.mtp.scanner.feature_login.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.R
import com.mtp.scanner.common.RESET_TOKEN
import com.mtp.scanner.common.SnackBarUtil
import com.mtp.scanner.databinding.FragmentResetPasswordBinding
import com.mtp.scanner.feature_login.domain.model.ResetPasswordUIState
import com.mtp.scanner.utils.DialogProgressUtil
import com.mtp.scanner.utils.NavigateFragmentUtil.navigateWithClearAllBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private var resetToken = ""
    private val viewModel: LoginViewModel by activityViewModels()

    private var newPassword= ""
    private var conPassword= ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResetPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            resetToken = it.getString(RESET_TOKEN, "")

        }

        initView()
    }

    private fun initView() {

        binding.etPass.doAfterTextChanged {
            if (it.toString().isNotEmpty()){
                newPassword = it.toString().trim()
            }
        }

        binding.etConfirmPass.doAfterTextChanged {
            if (it.toString().isNotEmpty()){
                 conPassword = it.toString().trim()
            }
        }
        binding.btnReset.setOnClickListener {
            if (checkValidation()) {
                viewModel.resetPassword(newPassword,conPassword)
                observeResetAPIResponse()
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().navigateWithClearAllBackStack(R.id.forgotPasswordFragment)
        }
    }

    private fun observeResetAPIResponse() {
        viewModel.getResetPassword.observe(viewLifecycleOwner){
            when(it){
                is ResetPasswordUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is ResetPasswordUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.result.message.toString(),true)
                    findNavController().navigateWithClearAllBackStack(R.id.signInFragment)
                }
                is ResetPasswordUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onFailure)
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var isValid = false
        if (newPassword.isEmpty()) {
            SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.please_enter_new_pass))
        } else if (conPassword.isEmpty()) {
            SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.please_enter_con_pass))
        } else if (newPassword != conPassword) {
            SnackBarUtil.showCustomSnackBar(binding.root,getString(R.string.new_password_and_confirm_password_should_same))
        } else {
            isValid = true
        }
        return isValid
    }
}