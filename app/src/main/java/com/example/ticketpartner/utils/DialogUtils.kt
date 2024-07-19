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


    fun customAlertDialog(context: Context,title: String,message: String): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle(title)
            setMessage(message)
        }
        return builder
    }

}