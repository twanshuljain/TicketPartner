package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model

import com.mtp.ticketpartner.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData

sealed class GetCheckInDataLocalDBUIState {
    data class IsLoading(val isLoading: Boolean) : GetCheckInDataLocalDBUIState()
    data class OnSuccess(val onSuccess: List<CheckInData>) : GetCheckInDataLocalDBUIState()
    data class OnFailure(val onFailure: String) : GetCheckInDataLocalDBUIState()
}