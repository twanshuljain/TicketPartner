package com.example.ticketpartner.feature_create_event.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_add_organization.domain.model.SearchCountryUIState
import com.example.ticketpartner.feature_add_organization.domain.usecase.GetSearchCountryUseCase
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTicketListUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventVenueStateUIState
import com.example.ticketpartner.feature_create_event.domain.useCase.GetCreateEventTicketListUseCase
import com.example.ticketpartner.feature_create_event.domain.useCase.GetCreateEventTypesUseCase
import com.example.ticketpartner.feature_create_event.domain.useCase.GetCreateEventVenueStateUseCase
import com.example.ticketpartner.feature_create_event.domain.useCase.GetTimeZoneUseCase
import com.example.ticketpartner.feature_login.presentation.LoginViewModel
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    private val getTimeZoneUseCase: GetTimeZoneUseCase,
    private val getCreateEventTypesUseCase: GetCreateEventTypesUseCase,
    private val getSearchCountryUseCase: GetSearchCountryUseCase,
    private val getCreateEventVenueStateUseCase: GetCreateEventVenueStateUseCase,
    private val getCreateEventTicketListUseCase: GetCreateEventTicketListUseCase,
    private val logUtil: LogUtil
) : ViewModel() {

    private val _getTimeZone: MutableLiveData<CreateEventGetTimeZoneUIState> = MutableLiveData()
    val getTimeZoneResponse: LiveData<CreateEventGetTimeZoneUIState> = _getTimeZone

    private val _getEventType: MutableLiveData<CreateEventTypesUIState> = MutableLiveData()
    val getEventTypeResponse: LiveData<CreateEventTypesUIState> = _getEventType

    private val _searchCountry: MutableLiveData<SearchCountryUIState> =
        MutableLiveData()
    val getSearchCountry: LiveData<SearchCountryUIState> = _searchCountry

    private val _getStateResponse: MutableLiveData<CreateEventVenueStateUIState> =
        MutableLiveData()
    val getStateResponse: LiveData<CreateEventVenueStateUIState> = _getStateResponse

    private val _getTicketList: MutableLiveData<CreateEventTicketListUIState> =
        MutableLiveData()
    val getTicketListResponse: LiveData<CreateEventTicketListUIState> = _getTicketList

    fun getTimeZone() {
        _getTimeZone.value = CreateEventGetTimeZoneUIState.IsLoading(true)
        viewModelScope.launch {
            getTimeZoneUseCase.invoke().catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getTimeZone.value = CreateEventGetTimeZoneUIState.OnFailure(error.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _getTimeZone.value = CreateEventGetTimeZoneUIState.OnSuccess(it)
            }
        }
    }

    fun getEventType() {
        _getEventType.value = CreateEventTypesUIState.IsLoading(true)
        viewModelScope.launch {
            getCreateEventTypesUseCase.invoke().catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getEventType.value = CreateEventTypesUIState.OnFailure(error.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onCollect${it}")
                _getEventType.value = CreateEventTypesUIState.OnSuccess(it)
            }
        }
    }

    fun getCountryList() {
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

    fun getStateBasedOnCountry(countryId: Int) {
        _getStateResponse.value = CreateEventVenueStateUIState.IsLoading(true)
        viewModelScope.launch {
            getCreateEventVenueStateUseCase.invoke(countryId).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getStateResponse.value =
                    CreateEventVenueStateUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onSuccess: $it")
                _getStateResponse.value = CreateEventVenueStateUIState.OnSuccess(it)
            }
        }
    }

    fun getTicketList(eventId: Int) {
        _getTicketList.value = CreateEventTicketListUIState.IsLoading(true)
        viewModelScope.launch {
            getCreateEventTicketListUseCase.invoke(eventId).catch {
                logUtil.log(LoginViewModel.TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getTicketList.value =
                    CreateEventTicketListUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginViewModel.TAG, "onSuccess: $it")
                _getTicketList.value = CreateEventTicketListUIState.OnSuccess(it)
            }
        }

    }
}