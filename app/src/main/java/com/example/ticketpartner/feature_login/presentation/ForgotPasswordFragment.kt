package com.example.ticketpartner.feature_login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ticketpartner.databinding.FragmentForgotPasswordBinding
import com.example.ticketpartner.utils.FullScreenDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            // findNavController().navigate(R.id.resetPasswordFragment)


            val title = "Custom Title"
            val message = "This is a custom message for the full-screen dialog."
            val fullScreenDialog = FullScreenDialogFragment.newInstance(title, message)
            fullScreenDialog.show(requireActivity().supportFragmentManager, "FullScreenDialog")


            // findNavController().navigate(R.id.resetPasswordFragment)

        }

        /*  fullScreenDialog.dialog?.setOnCancelListener {
              findNavController().navigate(R.id.resetPasswordFragment)
          }*/
    }
}