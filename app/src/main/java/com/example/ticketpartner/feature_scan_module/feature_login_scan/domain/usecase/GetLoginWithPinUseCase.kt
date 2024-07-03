package com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.usecase

import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.LoginWithPinResponse
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.repository.LoginScanRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLoginWithPinUseCase @Inject constructor(private val loginScanRepository: LoginScanRepository) {

    private val loginFlow = MutableSharedFlow<LoginWithPinResponse>()
    suspend fun invoke(
        name: String,
        scanPin: String
    ): kotlinx.coroutines.flow.Flow<LoginWithPinResponse> {
        return flow {
            emit(loginScanRepository.loginScanWithPin(name, scanPin))
        }.flowOn(Dispatchers.IO)
    }

    /*    suspend fun invoke(name: String, scanPin: String): SharedFlow<LoginWithPinResponse> {
            // Ensure that the repository call is made on the IO dispatcher
            val response = withContext(Dispatchers.IO) {
                loginScanRepository.loginScanWithPin(name, scanPin)
            }
            // Emit the response in the SharedFlow
            loginFlow.emit(response)
            // Return the SharedFlow
            return loginFlow.asSharedFlow()
        }*/

}