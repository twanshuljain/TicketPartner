package com.example.ticketpartner.feature_add_organization.domain.model

sealed class AddOrganizationSocialUIState {
    data class IsLoading(val isLoading: Boolean = false) : AddOrganizationSocialUIState()
    data class OnSuccess(val result: AddOrgSocialResponse) : AddOrganizationSocialUIState()
    data class OnFailure(val onFailure: String) : AddOrganizationSocialUIState()
}