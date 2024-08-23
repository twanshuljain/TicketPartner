package com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model

import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.DataItems

sealed class QrCodeListFromLocalDBUIState {

    data class IsLoading(val isLoading: Boolean) : QrCodeListFromLocalDBUIState()
    data class OnSuccess(val onSuccess: List<DataItems>) : QrCodeListFromLocalDBUIState()
    data class OnFailure(val onFailure: String) : QrCodeListFromLocalDBUIState()
}