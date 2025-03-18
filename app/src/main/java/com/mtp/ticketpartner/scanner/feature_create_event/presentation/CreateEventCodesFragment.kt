package com.mtp.ticketpartner.scanner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mtp.scanner.R
import com.mtp.scanner.databinding.FragmentCreateEventCodesBinding


class CreateEventCodesFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventCodesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventCodesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.titleBar.tvTitle.text = getString(R.string.codes)

    }
}

