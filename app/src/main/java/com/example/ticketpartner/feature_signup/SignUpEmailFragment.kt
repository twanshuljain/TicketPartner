package com.example.ticketpartner.feature_signup

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.PLUS
import com.example.ticketpartner.databinding.FragmentSignUpEmailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class SignUpEmailFragment : Fragment() {
    private lateinit var binding: FragmentSignUpEmailBinding
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
        // Set up back press callback
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        var editTextValue = ""
        binding.etEmail.doAfterTextChanged {  editTextValue = binding.etEmail.text.toString() }

        /** navigate user to signIn page */
        binding.tvSignIn.setOnClickListener {
            val destinationIdToClearTo = R.id.signInFragment
                findNavController().popBackStack(destinationIdToClearTo, false)
        }

        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(EMAIL_KEY, editTextValue)
            findNavController().navigate(R.id.signUpDetailsFragment, bundle)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
                // If you don't want to handle the back press in this fragment, let the activity handle it
                isEnabled = false
                activity?.finish()
            }
        }
    }
