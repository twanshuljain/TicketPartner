package com.example.ticketpartner.feature_add_organization.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.EMAIL_KEY
import com.example.ticketpartner.common.SnackBarUtil
import com.example.ticketpartner.databinding.FragmentAddOrgCountrySearchBinding
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryUIState
import com.example.ticketpartner.feature_add_organization.domain.model.SearchItem
import com.example.ticketpartner.feature_add_organization.presentation.adapter.SearchCountryAdapter
import com.example.ticketpartner.utils.DialogProgressUtil
import com.example.ticketpartner.utils.NavigateFragmentUtil.clearBackStackToDestination

class AddOrgCountrySearchFragment : Fragment() {
    private lateinit var binding: FragmentAddOrgCountrySearchBinding
    private lateinit var searchCountryAdapter: SearchCountryAdapter
    private val viewModel: AddOrganizationViewModel by activityViewModels()
    private var searchCountryList: ArrayList<SearchItem?> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddOrgCountrySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
    }

    private fun initView() {
        viewModel.SearchCountry()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCountries(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun filterCountries(query: String) {
        val searchList = searchCountryList.filter { country ->
            country?.country_name.let { name ->
                name?.lowercase()?.contains(query.lowercase())!!
            }
        }
        searchCountryAdapter = SearchCountryAdapter(searchList,::onItemClick)
        binding.rvSearchCountry.adapter = searchCountryAdapter
        searchCountryAdapter.notifyDataSetChanged()
    }

    private fun observeLiveData() {
        viewModel.getSearchCountry.observe(viewLifecycleOwner) {
            when (it) {
                is SearchCountryUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }

                is SearchCountryUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    it.result.data?.let { list ->
                        setSearchAdapter(list)
                    }
                }

                is SearchCountryUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showErrorSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }

    private fun setSearchAdapter(data: List<SearchItem?>?) {
        searchCountryAdapter = SearchCountryAdapter(searchCountryList,::onItemClick)
        data?.let { searchCountryList.addAll(it) }
        binding.rvSearchCountry.adapter = searchCountryAdapter
    }
    private fun onItemClick(countryId: Int, countryName: String) {
        if (countryName.isNotEmpty()){
            val bundle = Bundle()
            //bundle.putString("countryId", countryId.toString())
            bundle.putString("countryName", countryName)

            //findNavController().navigate(R.id.addOrganizationChangeLogoFragment,bundle)
            Toast.makeText(requireContext(), countryName, Toast.LENGTH_SHORT).show()
            findNavController().clearBackStackToDestination(R.id.addOrganizationChangeLogoFragment)
        }
    }
}


