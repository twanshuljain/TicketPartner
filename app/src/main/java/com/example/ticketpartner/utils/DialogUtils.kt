package com.example.ticketpartner.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.ticketpartner.R
import com.example.ticketpartner.databinding.LayoutEndScanBottomDialogBinding
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

    fun sessionExpiredDialog(context: Context): AlertDialog.Builder {
 val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Session Expired")
            setMessage("Your session has expired. Please log in again to continue.")
            setPositiveButton("OK") { dialog, _ ->

            }
        }
return builder
    }

}