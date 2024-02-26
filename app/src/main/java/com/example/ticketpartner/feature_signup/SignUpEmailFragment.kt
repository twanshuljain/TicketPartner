package com.example.ticketpartner.feature_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ContactUsInputFieldValidator
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentSignUpEmailBinding
import com.example.ticketpartner.utils.NavigateFragmentUtil.clearBackStackToDestination
import com.example.ticketpartner.utils.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpEmailFragment : Fragment() {
    private lateinit var binding: FragmentSignUpEmailBinding
    var etEmail = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        /* // Set up back press callback
         requireActivity().onBackPressedDispatcher.addCallback(
             viewLifecycleOwner,
             onBackPressedCallback
         )*/

        binding.etEmail.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                etEmail = it.toString().trim()
                enableCreateAccountButton(true)
            } else {
                enableCreateAccountButton(false)
            }
        }


        /** navigate user to signIn page */
        binding.tvSignIn.setOnClickListener {
            findNavController().clearBackStackToDestination(R.id.signInFragment)
        }

        binding.btnSignUp.setOnClickListener {
            if (!ContactUsInputFieldValidator.isEmailValidPattern(etEmail)) {
                SnackBarUtil.showErrorSnackBar(
                    binding.root, getString(R.string.please_enter_valid_email)
                )
            } else {
                val bundle = Bundle()
                bundle.putString(EMAIL_KEY, etEmail)
                findNavController().navigate(R.id.signUpDetailsFragment, bundle)
            }

        }
    }

    private fun initView() {
        changeTextColor()
    }

    private fun enableCreateAccountButton(value: Boolean) {
        if (value) {
            binding.btnSignUp.visibility = View.VISIBLE
            binding.btnSignUpDisable.visibility = View.GONE
        } else {
            binding.btnSignUp.visibility = View.GONE
            binding.btnSignUpDisable.visibility = View.VISIBLE
        }
    }


    private fun changeTextColor() {
        val originalText = getString(R.string.privacy_policy)
        val wordsToColor = listOf("Terms of Service", "Privacy Policy.")
        val changedString = Utility.changeStringColor(originalText, wordsToColor)
        binding.tvPolicy.text = changedString
    }

    /*  private val onBackPressedCallback = object : OnBackPressedCallback(true) {
          override fun handleOnBackPressed() {
              // If you don't want to handle the back press in this fragment, let the activity handle it
              isEnabled = false
              activity?.finish()
          }
      }*/
}
