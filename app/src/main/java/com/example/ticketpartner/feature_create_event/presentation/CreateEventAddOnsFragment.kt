package com.example.ticketpartner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ticketpartner.databinding.FragmentCreateEventAddOnsBinding

class CreateEventAddOnsFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventAddOnsBinding
    private val viewModel: CreateEventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = FragmentCreateEventAddOnsBinding.inflate(layoutInflater)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

    }
}