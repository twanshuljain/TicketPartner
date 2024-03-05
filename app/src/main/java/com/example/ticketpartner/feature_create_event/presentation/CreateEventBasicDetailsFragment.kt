package com.example.ticketpartner.feature_create_event.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.ticketpartner.R
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentCreateEventBasicDetailsBinding
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
import com.example.ticketpartner.utils.DatePickerUtility
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.TimePickerUtility
import com.google.android.material.tabs.TabLayoutMediator

class CreateEventBasicDetailsFragment : Fragment() {
    private lateinit var binding: FragmentCreateEventBasicDetailsBinding
    private val viewModel: CreateEventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateEventBasicDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        makeGetApisCall()
        observeResponse()

        setTabViewAdapter()
        //spinner adapter
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.planetsSpinner.adapter = adapter
        }

        val languages = resources.getStringArray(R.array.planets_array)
        binding.planetsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    "selected Item " + " " +
                            "" + languages[position], Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        /*
                binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        Toast.makeText(
                            requireContext(),
                            "checked",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "unChecked",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }*/
    }

    private fun makeGetApisCall() {
        viewModel.getTimeZone()
    }

    private fun observeResponse() {
        viewModel.getTimeZoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is CreateEventGetTimeZoneUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is CreateEventGetTimeZoneUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showSuccessSnackBar(binding.root, it.result.message.toString())
                }

                is CreateEventGetTimeZoneUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun initView() {
        val viewDateTime = binding.includeDateTime
        val viewDoorOpen = binding.includeDateTime

        viewDateTime.startDate.tvName.text = "Start Date"
        viewDateTime.endDate.tvName.text = "End Date"

        viewDateTime.startTime.tvName.text = "Start Time"
        viewDateTime.endTime.tvName.text = "End Time"

        viewDateTime.startDateDoorOpen.tvName.text = "Start Date"
        viewDateTime.endDateDoorOpen.tvName.text = "End Date"

        viewDateTime.startTimeDoorOpen.tvName.text = "Start Time"
        viewDateTime.endTimeDoorOpen.tvName.text = "End Time"

        viewDateTime.startDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.startDate.tvDate)
        }

        viewDateTime.startTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDateTime.startTime.tvTime)
        }

        viewDateTime.endDate.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDateTime.endDate.tvDate)
        }

        viewDateTime.endTime.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDateTime.endTime.tvTime)
        }

        /** for door open's start-end date, start-end time */
        viewDoorOpen.startDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(
                requireContext(),
                viewDoorOpen.startDateDoorOpen.tvDate
            )
        }

        viewDoorOpen.startTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(
                requireContext(),
                viewDoorOpen.startTimeDoorOpen.tvTime
            )
        }

        viewDoorOpen.endDateDoorOpen.dateLayout.setOnClickListener {
            DatePickerUtility.getSelectedDate(requireContext(), viewDoorOpen.endDateDoorOpen.tvDate)
        }

        viewDoorOpen.endTimeDoorOpen.timeLayout.setOnClickListener {
            TimePickerUtility.getSelectedTime(requireContext(), viewDoorOpen.endTimeDoorOpen.tvTime)
        }

    }


    private fun setTabViewAdapter() {
        //list of tab title name
        val titleList = resources.getStringArray(R.array.tab_create_event)

        //tabView adapter
        val adapter = TabCreateEvent(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }
}