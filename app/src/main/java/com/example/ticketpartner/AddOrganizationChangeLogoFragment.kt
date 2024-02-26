package com.example.ticketpartner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.databinding.FragmentAddOrganizationChangeLogoBinding

class AddOrganizationChangeLogoFragment : Fragment() {
    private lateinit var binding: FragmentAddOrganizationChangeLogoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddOrganizationChangeLogoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.addOrganizationSocialFragment)
        }
    }

}