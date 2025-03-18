package com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mtp.scanner.R
import com.mtp.scanner.databinding.FragmentFindPinBinding


class WhereToFindPinFragment : Fragment() {
private lateinit var binding: FragmentFindPinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFindPinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
          findNavController().popBackStack(R.id.findPinFragment,true)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleOnBackPressedButton()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    private fun handleOnBackPressedButton() {
        findNavController().popBackStack(R.id.findPinFragment,true)
    }
}