package com.example.ticketpartner.feature_scan_module.feature_rfid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(): ViewModel() {

    private val _receiveNfcData: MutableLiveData<String> = MutableLiveData()
    val receiveNfcData: LiveData<String> = _receiveNfcData

    fun setNfcValur(data: String){
        _receiveNfcData.value = data
    }
}