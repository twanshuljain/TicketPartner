package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ticketpartner.databinding.FragmentScanSearchedOrderDetailsBinding

class ScanSearchedOrderDetailsFragment : Fragment() {
private lateinit var binding: FragmentScanSearchedOrderDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanSearchedOrderDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
      /*  val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.scanBottomNavSearchFragment)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            callback
        )
*/
        binding.includeTitle.subTitle.visibility = View.GONE

        /*binding.includeTitle.ivBack.setOnClickListener {
            findNavController().navigate(R.id.scanBottomNavSearchFragment)
        }*/
     }

}