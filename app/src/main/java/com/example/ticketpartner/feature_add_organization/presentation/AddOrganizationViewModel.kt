package com.example.ticketpartner.feature_add_organization.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrgSocialRequest
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationSocialUIState
import com.example.ticketpartner.feature_add_organization.domain.model.AddOrganizationUIState
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryUIState
import com.example.ticketpartner.feature_add_organization.domain.usecase.GetAddOrgSocialUseCase
import com.example.ticketpartner.feature_add_organization.domain.usecase.GetAddOrganizationUseCase
import com.example.ticketpartner.feature_add_organization.domain.usecase.GetSearchCountryUseCase
import com.example.ticketpartner.feature_login.presentation.LoginViewModel
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddOrganizationViewModel @Inject constructor(
    private val getAddOrgUseCase: GetAddOrganizationUseCase,
    private val getAddOrgSocialUseCase: GetAddOrgSocialUseCase,
    private val getSearchCountryUseCase: GetSearchCountryUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _addOrganization: MutableLiveData<AddOrganizationUIState> = MutableLiveData()
    val getAddOrganization: LiveData<AddOrganizationUIState> = _addOrganization

    private val _addOrganizationSocial: MutableLiveData<AddOrganizationSocialUIState> =
        MutableLiveData()
    val getAddOrganizationSocial: LiveData<AddOrganizationSocialUIState> = _addOrganizationSocial

    private val _searchCountry: MutableLiveData<SearchCountryUIState> =
        MutableLiveData()
    val getSearchCountry: LiveData<SearchCountryUIState> = _searchCountry

    private val _addCountryName = MutableLiveData<String>()
    val getCountyName: LiveData<String> = _addCountryName

    private val _addCountryId = MutableLiveData<String>()
    val getCountyId: LiveData<String> = _addCountryId

    fun updateSearchCountryName(newValue: String) {
        _addCountryName.value = newValue
    }

    fun updateSearchCountryId(newValue: String) {
        _addCountryId.value = newValue
    }


    fun addOrganization(file: File, name: String, CountryId: String) {
        _addOrganization.value = AddOrganizationUIState.IsLoading(true)
        viewModelScope.launch {
            getAddOrgUseCase.invoke(file, name, CountryId).catch {
                logUtil.log(
                    LoginViewModel.TAG,
                    "onError${it.message.toString()}"
                )
                val error = ErrorResponseHandler(it)
                _addOrganization.value =
                    AddOrganizationUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _addOrganization.value = AddOrganizationUIState.OnSuccess(it)
            }
        }
    }

    fun addOrganizationSocialPage(addOrgSocialRequest: AddOrgSocialRequest) {
        _addOrganizationSocial.value = AddOrganizationSocialUIState.IsLoading(true)
        viewModelScope.launch {
            getAddOrgSocialUseCase.invoke(addOrgSocialRequest).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _addOrganizationSocial.value =
                    AddOrganizationSocialUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onSuccess: $it")
                _addOrganizationSocial.value = AddOrganizationSocialUIState.OnSuccess(it)
            }
        }
    }

    fun SearchCountry() {
        _searchCountry.value = SearchCountryUIState.IsLoading(true)
        viewModelScope.launch {
            getSearchCountryUseCase.invoke().catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _searchCountry.value =
                    SearchCountryUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onSuccess: $it")
                _searchCountry.value = SearchCountryUIState.OnSuccess(it)
            }
        }
    }
}