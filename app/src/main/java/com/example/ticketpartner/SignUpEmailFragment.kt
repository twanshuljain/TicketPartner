package com.example.ticketpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.databinding.FragmentSignUpEmailBinding

class SignUpEmailFragment : Fragment() {
    private lateinit var binding: FragmentSignUpEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        bundle.putString("email", binding.etEmail.text.toString())

        /** navigate user to signIn page */
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.signInFragment, bundle)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.signUpDetailsFragment)
        }
    }
}