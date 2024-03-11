package com.example.ticketpartner.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

class OtpChangeFocusUtil {
  companion object {
        fun changeFocus(editTextFieldList: MutableList<EditText>) {
            for (i in 0 until editTextFieldList.size) {
                val currentField = editTextFieldList[i]
                // Set a TextWatcher to check when the current field is filled
                currentField.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                        // No action needed
                    }
                    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                        // No action needed
                    }
                    override fun afterTextChanged(editable: Editable?) {
                        if (editable?.length == 1 && i < editTextFieldList.size - 1) {
                            // Automatically move to the next field if the current field is filled
                            editTextFieldList[i + 1].requestFocus()
                        }
                    }
                })
                // Set an OnKeyListener to clear the current field on back press
                currentField.setOnKeyListener { _, keyCode, keyEvent ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.action == KeyEvent.ACTION_DOWN) {
                        // Clear the current field on back press
                        currentField.text.clear()
                        // Move to the previous field if available
                        if (i > 0) {
                            editTextFieldList[i - 1].requestFocus()
                        }
                        return@setOnKeyListener true
                    }
                    false
                }
            }
        }
  }
}