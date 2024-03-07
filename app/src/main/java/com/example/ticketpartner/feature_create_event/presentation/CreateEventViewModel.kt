package com.example.ticketpartner.feature_create_event.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTypesUIState
import com.example.ticketpartner.feature_create_event.domain.useCase.GetCreateEventTypesUseCase
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
    private val logUtil: LogUtil
) : ViewModel() {

    private val _getTimeZone: MutableLiveData<CreateEventGetTimeZoneUIState> = MutableLiveData()
    val getTimeZoneResponse: LiveData<CreateEventGetTimeZoneUIState> = _getTimeZone

    private val _getEventType: MutableLiveData<CreateEventTypesUIState> = MutableLiveData()
    val getEventTypeResponse: LiveData<CreateEventTypesUIState> = _getEventType

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
}