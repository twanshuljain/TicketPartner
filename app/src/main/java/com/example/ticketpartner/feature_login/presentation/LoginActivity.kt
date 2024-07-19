package com.example.ticketpartner.feature_login.presentation

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.ticketpartner.R
import com.example.ticketpartner.feature_scan_module.feature_rfid.NfcViewModel
import com.example.ticketpartner.utils.NFCUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private  var mNfcAdapter: NfcAdapter? = null
    private val viewModel: NfcViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.main_nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        val manager = getSystemService(Context.NFC_SERVICE) as NfcManager
        mNfcAdapter = manager.defaultAdapter
        mNfcAdapter?.let {
            if (it.isEnabled) {
                NFCUtil.enableNFCInForeground(it, this, javaClass)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val nfcMessage = NFCUtil.retrieveNFCMessage(intent)
        val nfcId = NFCUtil.getNfcId(intent)
        viewModel.setNfcValur(nfcMessage)

    }
}