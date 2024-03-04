package com.example.ticketpartner.utils

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.FragmentFullScreenDialogBinding

class FullScreenDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentFullScreenDialogBinding
    private var title: String? = null
    private var message: String? = null
    var mDialogInterface: DialogInterface? = null

    companion object {
        fun newInstance(title: String, message: String): FullScreenDialogFragment {
            val dialog = FullScreenDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("message", message)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFullScreenDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                // Retrieve custom strings from arguments
                title = arguments?.getString("title")
                message = arguments?.getString("message")

                // Set custom strings in your dialog layout
               binding.titleTextView.text = title
                binding.messageTextView.text = message
    }

    override fun onStart() {
        super.onStart()

        //setDialogMargins()
        val window: Window? = dialog?.window
        val params: WindowManager.LayoutParams? = window?.attributes

        params?.dimAmount = 0.7f // Adjust dim amount if needed
        window?.attributes = params
        dialog?.window?.setBackgroundDrawableResource(R.drawable.full_screen_dialog_design)
        dialog?.window?.attributes?.gravity = Gravity.TOP
        dialog?.setCanceledOnTouchOutside(true)

/*        //set with and height of layout
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT*/

        //set margin vertically from top
        params?.y = resources.getDimensionPixelSize(R.dimen.margin_20)

        //set margin horizontally
      //  params?.x = resources.getDimensionPixelSize(R.dimen.margin_30)

        window?.attributes = params

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mDialogInterface = dialog
        hasDialogDismissed(true)
       // findNavController().navigate(R.id.resetPasswordFragment)
        // Perform actions on dialog dismiss (outer click)
        // This code will be executed when the dialog is dismissed, including outer clicks
        // Add your desired logic here
        findNavController().navigate(R.id.resetPasswordFragment)
    }

     fun hasDialogDismissed(value: Boolean = false):Boolean{
        return value
    }
}