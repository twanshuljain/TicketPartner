package com.mtp.ticketpartner.scanner.feature_add_organization.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.ORGANIZATION_ID
import com.mtp.ticketpartner.scanner.common.SnackBarUtil
import com.mtp.ticketpartner.scanner.common.remote.apis.SessionHandlerInterceptor
import com.mtp.ticketpartner.scanner.databinding.FragmentAddOrganizationSocialBinding
import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.AddOrganizationSocialUIState
import com.mtp.ticketpartner.scanner.utils.DialogProgressUtil
import com.mtp.ticketpartner.scanner.utils.NavigateFragmentUtil.navigateWithClearAllBackStack

class AddOrganizationSocialFragment : Fragment() {
    private lateinit var binding: FragmentAddOrganizationSocialBinding
    private val viewModel: AddOrganizationViewModel by activityViewModels()
    private var etWebsite = ""
    private var etFaceBook = ""
    private var etTwitter = ""
    private var etLinkedIn = ""
    private var organizationId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddOrganizationSocialBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            organizationId = it.getString(ORGANIZATION_ID, "")
        }

        Log.e(SessionHandlerInterceptor.TAG, "observeResponseData: organization id --> ${organizationId}", )

        binding.etWebSite.doAfterTextChanged {
            etWebsite = it.toString()
        }
        binding.etFacebook.doAfterTextChanged {
            etFaceBook = it.toString()
        }
        binding.etTwitter.doAfterTextChanged {
            etTwitter = it.toString()
        }
        binding.etLinkedIn.doAfterTextChanged {
            etLinkedIn = it.toString()
        }

        binding.titleName.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaaveAndContinue.setOnClickListener {
          /*  val request = AddOrgSocialRequest(
                facebook_url = etFaceBook,
                linkedin_url = etLinkedIn,
                organization_id = organizationId,
                twitter_url = etTwitter,
                website_url = etWebsite
            )
            if (organizationId.isNotEmpty()){
                viewModel.addOrganizationSocialPage(request)
                observeLiveData()
            }else{
                SnackBarUtil.showErrorSnackBar(binding.root, getString(R.string.empty_organization_id))
            }*/

            findNavController().navigate(R.id.createEventBasicDetaills)
        }
    }

    private fun observeLiveData() {
        viewModel.getAddOrganizationSocial.observe(viewLifecycleOwner){
            when(it){
                is  AddOrganizationSocialUIState.IsLoading -> {
                    DialogProgressUtil.show(childFragmentManager)
                }
                is  AddOrganizationSocialUIState.OnSuccess -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root, it.result.message.toString(),true)
                    findNavController().navigateWithClearAllBackStack(R.id.signInFragment)
                }
                is  AddOrganizationSocialUIState.OnFailure -> {
                    DialogProgressUtil.dismiss()
                    SnackBarUtil.showCustomSnackBar(binding.root, it.onFailure)
                }
            }
        }
    }
}