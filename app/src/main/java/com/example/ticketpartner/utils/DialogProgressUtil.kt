package com.example.ticketpartner.utils

import androidx.fragment.app.FragmentManager

class DialogProgressUtil {

    companion object {
        private var progressDialog: BaseProgressDialog? = null
        private var progressMessageDialog: BaseProgressMessageDialog? = null
        private var fragmentManager: FragmentManager? = null

        fun show(manager: FragmentManager) {
            fragmentManager = manager
            if (progressDialog == null) {
                progressDialog = BaseProgressDialog()
            }
            progressDialog?.let { dialog ->
                fragmentManager?.let { fm ->
                    if (!dialog.isAdded && fm.findFragmentByTag(BaseProgressDialog.DIALOG_TAG) == null) {
                        dialog.show(fm, BaseProgressDialog.DIALOG_TAG)
                        fm.executePendingTransactions()
                    }
                }
            }
        }

        fun showWithMessage(manager: FragmentManager, message: String) {
            fragmentManager = manager
            if (progressMessageDialog == null) {
                progressMessageDialog = BaseProgressMessageDialog(message)
            }
            progressMessageDialog?.let { dialog ->
                fragmentManager?.let { fm ->
                    if (!dialog.isAdded && fm.findFragmentByTag(BaseProgressMessageDialog.DIALOG_TAG) == null) {
                        dialog.show(fm, BaseProgressMessageDialog.DIALOG_TAG)
                        fm.executePendingTransactions()
                    }
                }
            }
        }

        fun dismissMessageDialog() {
            progressMessageDialog?.let { dialog ->
                fragmentManager?.let { fm ->
                    if (!fm.isDestroyed && dialog.isAdded)
                        dialog.dismiss()
                }
            }
        }

        fun dismiss() {
            progressDialog?.let { dialog ->
                fragmentManager?.let { fm ->
                    if (!fm.isDestroyed && dialog.isAdded)
                        dialog.dismiss()
                }
            }
        }
    }
}