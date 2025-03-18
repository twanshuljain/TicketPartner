package com.mtp.ticketpartner.scanner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.SnackBarUtil
import com.mtp.ticketpartner.scanner.databinding.FragmentCreateEventTicketsBinding
import com.mtp.ticketpartner.scanner.databinding.LayoutBottomSheetEditDeleteBinding
import com.mtp.ticketpartner.scanner.feature_create_event.domain.model.CreateEventTicketListResponse
import com.mtp.ticketpartner.scanner.feature_create_event.domain.model.CreateEventTicketListUIState
import com.mtp.ticketpartner.scanner.feature_create_event.presentation.adapter.CreateEventTicketListAdapter
import com.mtp.ticketpartner.scanner.utils.DialogProgressUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEventTicketsFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventTicketsBinding
    private val viewModel: CreateEventViewModel by viewModels()
    private lateinit var adapter: CreateEventTicketListAdapter
    private var ticketListResponse = ArrayList<CreateEventTicketListResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventTicketsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewModel.getTicketList(372)
        viewModel.getTicketListResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventTicketListUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventTicketListUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    if (!it.result.data.isNullOrEmpty()) {
                        ticketListResponse.add(it.result)
                        adapter = CreateEventTicketListAdapter(
                            requireContext(),
                            ticketListResponse,
                            ::onMinusClick,
                            ::onPlusClick,
                            ::onEllipsisClick
                        )
                        binding.rvTicketList.adapter = adapter
                    }
                }

                is CreateEventTicketListUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root,it.onFailure)
                }
            }
        }
    }


    private fun openEditDeleteBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutBottomSheetEditDeleteBinding.inflate(layoutInflater)
        dialogView.tvEdit.setOnClickListener {
            onEditBtnClick()
            dialog.dismiss()
        }
        dialogView.tvDelete.setOnClickListener {
            onDeleteClick()
            dialog.dismiss()
        }
        dialog.setContentView(dialogView.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private fun onDeleteClick() {
        Toast.makeText(requireContext(), "Item Deleted", Toast.LENGTH_SHORT).show()
    }

    private fun onEditBtnClick() {
        Toast.makeText(requireContext(), "Item Edited", Toast.LENGTH_SHORT).show()
    }

    private fun initView() {
        binding.titleBar.tvTitle.text = getString(R.string.create_event)

        binding.tvAddOns.setOnClickListener {

        }

        binding.titleBar.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onPlusClick(position: Int) {

    }

    private fun onMinusClick(position: Int) {

    }

    private fun onEllipsisClick() {
        openEditDeleteBottomSheet()
    }
}