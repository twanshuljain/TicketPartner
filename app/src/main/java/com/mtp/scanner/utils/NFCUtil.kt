package com.mtp.scanner.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Build
import com.mtp.scanner.common.NFC_TAG_NOT_TAPPED

object NFCUtil {

    fun <T> enableNFCInForeground(nfcAdapter: NfcAdapter, activity: Activity, classType: Class<T>) {
        val pendingIntent : PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                activity,
                0,
                Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                activity, 0,
                Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
        }
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null)
    }


    fun retrieveNFCMessage(intent: Intent?): String {
        intent?.let {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                val nDefMessages = getNDefMessages(intent)
                nDefMessages[0].records?.let {
                    it.forEach {
                        it?.payload.let {
                            it?.let {
                                return String(it)

                            }
                        }
                    }
                }

            } else {
                return NFC_TAG_NOT_TAPPED
            }
        }
        return NFC_TAG_NOT_TAPPED
    }

    fun getNDefMessages(intent: Intent): Array<NdefMessage> {
        val rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        rawMessage?.let {
            return rawMessage.map {
                it as NdefMessage
            }.toTypedArray()
        }

        val empty = byteArrayOf()
        val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty)
        val msg = NdefMessage(arrayOf(record))
        return arrayOf(msg)
    }

    fun getNfcId(intent: Intent?): String {
        intent?.let {
            return when {
                NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action -> {
                    ndefMessage(intent)

                }
                NfcAdapter.ACTION_TAG_DISCOVERED == intent.action -> {
                    ndefMessage(intent)

                }
                else -> NFC_TAG_NOT_TAPPED
            }
        }

        return NFC_TAG_NOT_TAPPED
    }

    private fun ndefMessage(intent: Intent?): String {
        return byteArrayToHexString(intent)
    }

    private fun byteArrayToHexString(intent: Intent?): String {
        val inArray = intent?.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        var i: Int
        var j = 0
        var inputS: Int
        val hex =
            arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var out = ""
        while (j < inArray!!.size) {
            inputS = inArray[j].toInt() and 0xff
            i = inputS shr 4 and 0x0f
            out += hex[i]
            i = inputS and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }

}