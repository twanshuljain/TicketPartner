package com.example.ticketpartner.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.common.storage.MyPreferences
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
import com.example.ticketpartner.utils.NavigateFragmentUtil.navigateWithClearNavGraph
import com.google.android.material.bottomsheet.BottomSheetDialog

object DialogUtils {
    fun showLogoutDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        dialog: BottomSheetDialog,
        message: String
    ): LayoutEndScanBottomDialogBinding {
        val dialogView = LayoutEndScanBottomDialogBinding.inflate(layoutInflater)
        dialogView.apply {
            tvTitle.text = context.getString(R.string.logout)
            tvDescription.text = message
        }
        dialogView.btnNo.setOnClickListener {
            dialog.dismiss()
        }

        dialogView.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(dialogView.root)
        dialog.show()
        return dialogView
    }

}