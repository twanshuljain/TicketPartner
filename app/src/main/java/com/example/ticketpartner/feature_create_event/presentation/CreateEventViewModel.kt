package com.example.ticketpartner.feature_create_event.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketpartner.common.LogUtil
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventGetTimeZoneUIState
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
    private val logUtil: LogUtil
) : ViewModel() {

    private val _getTimeZone: MutableLiveData<CreateEventGetTimeZoneUIState> = MutableLiveData()
    val getTimeZoneResponse: LiveData<CreateEventGetTimeZoneUIState> = _getTimeZone
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
}