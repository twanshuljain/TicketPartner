package com.mtp.ticketpartner.scanner.feature_add_organization.domain.model

sealed class AddOrganizationUIState {
    data class IsLoading(val isLoading: Boolean = false) : AddOrganizationUIState()
    data class OnSuccess(val result: AddOrganizationResponse) : AddOrganizationUIState()
    data class OnFailure(val onFailure: String) : AddOrganizationUIState()
}