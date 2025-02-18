package com.mtp.scanner.feature_scan_module.feature_qr_scan.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mtp.scanner.common.LogUtil
import com.mtp.scanner.common.ZERO
import com.mtp.scanner.common.localDatabase.TPLocalDatabase
import com.mtp.scanner.common.remote.apis.UNAUTHORIZED_USER
import com.mtp.scanner.feature_local_storage.domain.usecase.GetQrCodeListFromLocalDBUseCase
import com.mtp.scanner.feature_local_storage.domain.usecase.InsertTicketTypesOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.CheckInData
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.EventDetailsScanUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertCheckInDataOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertSearchDataOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypesOfflineScanUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.QrScanSearchItemUIState
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.SearchData
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase.InsertCheckInDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.usecase.InsertSearchDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_login_scan.presentation.LoginScanVewModel
import com.mtp.scanner.feature_scan_module.feature_login_scan.presentation.LoginScanVewModel.Companion
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.DeleteScanLogDataUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetCheckInDataLocalDBUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanLogOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanReportDataOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanReportTicketListOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetScanSearchDataOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.GetTicketTypesListOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.InsertScanLogOfflineUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrCodeListFromLocalDBUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanCheckedInUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanOrderDetailsUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanReportAllUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScanUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.QrScannedTicketUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.ScanLog
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.SendScanLogOfflineRequest
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.UploadScanDataServerUIState
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.DeleteScanLogDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetCheckInDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanReportAllUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanSearchUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScanUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetQrScannedTicketDataUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanCheckedInUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanEventDetailsDashboardUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanLogOfflineDataUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanOrderDetailsUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanReportDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetScanReportTicketListOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetSearchDataOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.GetTicketTypesListOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.InsertScanLogOfflineUseCase
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.usecase.UploadScanLogDataServerUseCase
import com.technotoil.tglivescan.common.retrofit.apis.ErrorResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrScanViewModel @Inject constructor(
    private val getQrScanUseCase: GetQrScanUseCase,
    private val getQrScannedTicketDataUseCase: GetQrScannedTicketDataUseCase,
    private val getScanEventDetailsDashboardUseCase: GetScanEventDetailsDashboardUseCase,
    private val getQrScanSearchUseCase: GetQrScanSearchUseCase,
    private val getScanOrderDetailsUseCase: GetScanOrderDetailsUseCase,
    private val getScanCheckedInUseCase: GetScanCheckedInUseCase,
    private val getQrScanReportAllUseCase: GetQrScanReportAllUseCase,
    private val getQrCodeListFromLocalDBUseCase: GetQrCodeListFromLocalDBUseCase,
    private val getTicketTypesListOfflineUseCase: GetTicketTypesListOfflineUseCase,
    private val getCheckInDataOfflineUseCase: GetCheckInDataOfflineUseCase,
    private val insertCheckInDataOfflineUseCase: InsertCheckInDataOfflineUseCase,
    private val getSearchDataOfflineUseCase: GetSearchDataOfflineUseCase,
    private val insertScanLogOfflineUseCase: InsertScanLogOfflineUseCase,
    private val insertSearchDataOfflineUseCase: InsertSearchDataOfflineUseCase,
    private val getScanLogOfflineDataUseCase: GetScanLogOfflineDataUseCase,
    private val uploadScanLogDataServerUseCase: UploadScanLogDataServerUseCase,
    private val deleteScanLogDataOfflineUseCase: DeleteScanLogDataOfflineUseCase,
    private val getScanReportDataOfflineUseCase: GetScanReportDataOfflineUseCase,
    private val getScanReportTicketListOfflineUseCase: GetScanReportTicketListOfflineUseCase,
    private val insertTicketTypesOfflineUseCase: InsertTicketTypesOfflineUseCase,
    private val logUtil: LogUtil,
    private val database: TPLocalDatabase
) : ViewModel() {

    val _qrScanState: MutableLiveData<QrScanUIState> = MutableLiveData()
    val observeQrScanResponse: LiveData<QrScanUIState> = _qrScanState

    private val _getScannedTicketState: MutableLiveData<QrScannedTicketUIState> = MutableLiveData()
    val observeTicketScannedResponse: LiveData<QrScannedTicketUIState> = _getScannedTicketState

    private val _getScanEventDetails: MutableLiveData<EventDetailsScanUIState> = MutableLiveData()
    val observeScanEventDetailsResponse: LiveData<EventDetailsScanUIState> = _getScanEventDetails

    private val _getScanEventDetailsHome: MutableLiveData<EventDetailsScanUIState> =
        MutableLiveData()
    val observeScanEventDetailsHomeResponse: LiveData<EventDetailsScanUIState> =
        _getScanEventDetailsHome

    private val _getScanSearchData: MutableLiveData<QrScanSearchItemUIState> = MutableLiveData()
    val observeScanSearchData: LiveData<QrScanSearchItemUIState> = _getScanSearchData

    private val _getScanOrderDetailsData: MutableLiveData<QrScanOrderDetailsUIState> =
        MutableLiveData()
    val observeScanOrderDetailsData: LiveData<QrScanOrderDetailsUIState> = _getScanOrderDetailsData

    private val _getScanCheckedInData: MutableLiveData<QrScanCheckedInUIState> = MutableLiveData()
    val observeScanCheckedInData: LiveData<QrScanCheckedInUIState> = _getScanCheckedInData

    private val _getScanReportAllData: MutableLiveData<QrScanReportAllUIState> = MutableLiveData()
    val getScanReportAllData: LiveData<QrScanReportAllUIState> = _getScanReportAllData

    private val _getQrCodeListFromLocalDB: MutableLiveData<QrCodeListFromLocalDBUIState> =
        MutableLiveData()
    val getQrCodeListFromLocalDB: LiveData<QrCodeListFromLocalDBUIState> = _getQrCodeListFromLocalDB

    private val _getTicketTypesListFromLocalDB: MutableLiveData<GetTicketTypesListOfflineUIState> =
        MutableLiveData()
    val getTicketTypesListFromLocalDB: LiveData<GetTicketTypesListOfflineUIState> =
        _getTicketTypesListFromLocalDB

    private val _getCheckInFromLocalDB: MutableLiveData<GetCheckInDataLocalDBUIState> =
        MutableLiveData()
    val getCheckInFromLocalDB: LiveData<GetCheckInDataLocalDBUIState> = _getCheckInFromLocalDB

    private val _insertCheckInDataOfflineScan: MutableLiveData<InsertCheckInDataOfflineUIState> =
        MutableLiveData()
    val insertCheckInDataOfflineScan: LiveData<InsertCheckInDataOfflineUIState> =
        _insertCheckInDataOfflineScan

    private val _getScanSearchDataFromLocalDB: MutableLiveData<GetScanSearchDataOfflineUIState> =
        MutableLiveData()
    val getScanSearchDataFromLocalDB: LiveData<GetScanSearchDataOfflineUIState> =
        _getScanSearchDataFromLocalDB

    private val _insertScanLogLocalDB: MutableLiveData<InsertScanLogOfflineUIState> =
        MutableLiveData()
    val insertScanLogLocalDB: LiveData<InsertScanLogOfflineUIState> = _insertScanLogLocalDB

    private val _getScanLogLocalDB: MutableLiveData<GetScanLogOfflineUIState> = MutableLiveData()
    val getScanLogLocalDB: LiveData<GetScanLogOfflineUIState> = _getScanLogLocalDB

    private val _selectedSearchOrderListData: MutableLiveData<List<SearchData>> = MutableLiveData()
    val selectedSearchOrderListData: LiveData<List<SearchData>> = _selectedSearchOrderListData

    private val _getScanReportDataFromLocalDB: MutableLiveData<GetScanReportDataOfflineUIState> =
        MutableLiveData()
    val getScanReportDataFromLocalDB: LiveData<GetScanReportDataOfflineUIState> =
        _getScanReportDataFromLocalDB

    private val _getScanReportTicketListFromLocalDB: MutableLiveData<GetScanReportTicketListOfflineUIState> =
        MutableLiveData()
    val getScanReportTicketListFromLocalDB: LiveData<GetScanReportTicketListOfflineUIState> =
        _getScanReportTicketListFromLocalDB

    private val _insertTicketTypesOfflineScan: MutableLiveData<InsertTicketTypesOfflineScanUIState> =
        MutableLiveData()
    val observeInsertTicketTypesOfflineScan: LiveData<InsertTicketTypesOfflineScanUIState> =
        _insertTicketTypesOfflineScan

    fun putSelectedOrderList(searchFilterData: List<SearchData>) {
        _selectedSearchOrderListData.value = searchFilterData
    }

    private val _insertSearchDataOfflineScan: MutableLiveData<InsertSearchDataOfflineUIState> =
        MutableLiveData()
    val insertSearchDataOfflineScan: LiveData<InsertSearchDataOfflineUIState> =
        _insertSearchDataOfflineScan

    private val _uploadScanLogDataOnServer: MutableLiveData<UploadScanDataServerUIState> =
        MutableLiveData()
    val uploadScanLogDataOnServer: LiveData<UploadScanDataServerUIState> =
        _uploadScanLogDataOnServer

    private val _deleteScanLogDataFromLocalDB: MutableLiveData<DeleteScanLogDataUIState> =
        MutableLiveData()
    val deleteScanLogDataFromLocalDB: LiveData<DeleteScanLogDataUIState> =
        _deleteScanLogDataFromLocalDB


    val onContinueClick = MutableLiveData<Int>()
    val selectedTicketTypeArrayList = MutableLiveData<ArrayList<String>>()
    var listSize = ZERO
    val eventName = MutableLiveData<String>()
    val dateTimeEventDetails = MutableLiveData<String>()
    val selectedSearchedItemEmailAdd = MutableLiveData<String>()
    val isOnlineMode = MutableLiveData<Boolean>()
    val isAllChecked = MutableLiveData<Boolean>()
    val isNetworkAvailableObserver = MutableLiveData<Boolean>()
    var isNetworkAvailable: Boolean = false
    var scanLogListSize: Int = ZERO

    //value offline scan count
    var totalScanned = ZERO
    var totalAccepted = ZERO
    var totalRejected = ZERO

    fun qrScanCode(qrId: String, ticketType: ArrayList<String>) {
        _qrScanState.value = QrScanUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScanUseCase.invoke(qrId, ticketType).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                logUtil.log(TAG, "onErrorQrResponse${error.getErrors().data}")

                val dataMap = error.getErrors()?.data as? Map<String, Any>
                val customerName = dataMap?.get("customer_name") as? String

                _qrScanState.value = QrScanUIState.OnFailure(error.getErrors().message.toString(), customerName ?: "")
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _qrScanState.value = QrScanUIState.OnSuccess(it)
            }
        }
    }

    fun getScannedTicketData() {
        _getScannedTicketState.value = QrScannedTicketUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScannedTicketDataUseCase.invoke().catch {
                val error = ErrorResponseHandler(it)
                logUtil.log(TAG, "onError -->> ${error.getErrors()}")
                if (error.getErrors().status_code == UNAUTHORIZED_USER.toString()) {
                    _getScannedTicketState.value =
                        QrScannedTicketUIState.OnFailure(UNAUTHORIZED_USER.toString())
                } else {
                    _getScannedTicketState.value =
                        QrScannedTicketUIState.OnFailure(error.getErrors().message.toString())
                }
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScannedTicketState.value = QrScannedTicketUIState.OnSuccess(it)
            }
        }
    }

    fun getEventDetailsData() {
        _getScanEventDetails.value = EventDetailsScanUIState.IsLoading(true)
        viewModelScope.launch {
            getScanEventDetailsDashboardUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanEventDetails.value =
                    EventDetailsScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanEventDetails.value = EventDetailsScanUIState.OnSuccess(it)
            }
        }
    }

    fun getEventDetailsHomeData() {
        _getScanEventDetailsHome.value = EventDetailsScanUIState.IsLoading(true)
        viewModelScope.launch {
            getScanEventDetailsDashboardUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanEventDetailsHome.value =
                    EventDetailsScanUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanEventDetailsHome.value = EventDetailsScanUIState.OnSuccess(it)
            }
        }
    }


    fun getSearchData(orderId: String) {
        viewModelScope.launch {
            getQrScanSearchUseCase.invoke(orderId).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)

                if (error.getErrors().status_code == UNAUTHORIZED_USER.toString()) {
                    _getScanSearchData.value =
                        QrScanSearchItemUIState.OnFailure(UNAUTHORIZED_USER.toString())
                } else {
                    _getScanSearchData.value =
                        QrScanSearchItemUIState.OnFailure(error.getErrors().message.toString())
                }
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanSearchData.value = QrScanSearchItemUIState.OnSuccess(it)
            }
        }
    }

    fun getOrderDetailsResponse(orderId: String) {
        _getScanOrderDetailsData.value = QrScanOrderDetailsUIState.IsLoading(true)
        viewModelScope.launch {
            getScanOrderDetailsUseCase.invoke(orderId).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanOrderDetailsData.value =
                    QrScanOrderDetailsUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: $it")
                _getScanOrderDetailsData.value = QrScanOrderDetailsUIState.OnSuccess(it)
            }
        }
    }

    fun getCheckInOrder(checkedOrderId: ArrayList<Long>, orderId: String) {
        _getScanCheckedInData.value = QrScanCheckedInUIState.IsLoading(true)
        viewModelScope.launch {
            getScanCheckedInUseCase.invoke(checkedOrderId, orderId).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanCheckedInData.value =
                    QrScanCheckedInUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: ${it.message}")
                _getScanCheckedInData.value = QrScanCheckedInUIState.OnSuccess(it)
            }
        }
    }

    fun getScanReportAllData(type: String) {
        _getScanReportAllData.value = QrScanReportAllUIState.IsLoading(true)
        viewModelScope.launch {
            getQrScanReportAllUseCase.invoke(type).catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                val error = ErrorResponseHandler(it)
                _getScanReportAllData.value =
                    QrScanReportAllUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse: ${it.message}")
                _getScanReportAllData.value = QrScanReportAllUIState.OnSuccess(it)
            }
        }
    }

    fun getQrCodeListFromLocalDB() {
        _getQrCodeListFromLocalDB.value = QrCodeListFromLocalDBUIState.IsLoading(true)
        viewModelScope.launch {
            getQrCodeListFromLocalDBUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _getQrCodeListFromLocalDB.value =
                    QrCodeListFromLocalDBUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse:")
                _getQrCodeListFromLocalDB.value = QrCodeListFromLocalDBUIState.OnSuccess(it)
            }
        }
    }

    fun getTicketTypesListFromLocal() {
        _getTicketTypesListFromLocalDB.value = GetTicketTypesListOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            getTicketTypesListOfflineUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _getTicketTypesListFromLocalDB.value =
                    GetTicketTypesListOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse:$it")
                _getTicketTypesListFromLocalDB.value =
                    GetTicketTypesListOfflineUIState.OnSuccess(it)
            }
        }
    }

    fun getCheckInDataFromLocalDB() {
        _getCheckInFromLocalDB.value = GetCheckInDataLocalDBUIState.IsLoading(true)
        viewModelScope.launch {
            getCheckInDataOfflineUseCase.invoke().catch {
                logUtil.log(TAG, "onError${it.message.toString()}")
                _getCheckInFromLocalDB.value =
                    GetCheckInDataLocalDBUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(TAG, "onResponse:$it")
                _getCheckInFromLocalDB.value = GetCheckInDataLocalDBUIState.OnSuccess(it)
            }
        }
    }

    fun insertCheckInDataOfflineScan(checkInData: CheckInData) {
        val arrayList = ArrayList<CheckInData>()
        if (arrayList.size> ZERO) arrayList.clear()
        arrayList.add(checkInData)
        _insertCheckInDataOfflineScan.value = InsertCheckInDataOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            insertCheckInDataOfflineUseCase.invoke(arrayList).catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _insertCheckInDataOfflineScan.value =
                    InsertCheckInDataOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse checkedINScanQR: $it")
                _insertCheckInDataOfflineScan.value =
                    InsertCheckInDataOfflineUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    fun getScanSearchDataOfflineScan() {
        _getScanSearchDataFromLocalDB.value = GetScanSearchDataOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            getSearchDataOfflineUseCase.invoke().catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _getScanSearchDataFromLocalDB.value =
                    GetScanSearchDataOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _getScanSearchDataFromLocalDB.value = GetScanSearchDataOfflineUIState.OnSuccess(it)
            }
        }
    }

    fun insertSearchDataOfflineScan(searchData: SearchData) {
        val arrayList = ArrayList<SearchData>()
        if (arrayList.size > ZERO) arrayList.clear()
        arrayList.add(searchData)
        _insertSearchDataOfflineScan.value = InsertSearchDataOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            insertSearchDataOfflineUseCase.invoke(arrayList).catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _insertSearchDataOfflineScan.value =
                    InsertSearchDataOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _insertSearchDataOfflineScan.value =
                    InsertSearchDataOfflineUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    fun insertScanLogOffline(scanLog: ScanLog) {
        _insertScanLogLocalDB.value = InsertScanLogOfflineUIState.IsLoading(false)
        viewModelScope.launch {
            insertScanLogOfflineUseCase.invoke(scanLog).catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _insertScanLogLocalDB.value =
                    InsertScanLogOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _insertScanLogLocalDB.value =
                    InsertScanLogOfflineUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    fun getScanLogListData() {
        _getScanLogLocalDB.value = GetScanLogOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            getScanLogOfflineDataUseCase.invoke().catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _getScanLogLocalDB.value = GetScanLogOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _getScanLogLocalDB.value = GetScanLogOfflineUIState.OnSuccess(it)
            }
        }
    }

    fun uploadScanLogDataOnServer(sendScanLogOfflineResponse: SendScanLogOfflineRequest) {
        _uploadScanLogDataOnServer.value = UploadScanDataServerUIState.IsLoading(true)
        viewModelScope.launch {
            uploadScanLogDataServerUseCase.invoke(sendScanLogOfflineResponse).catch {
                val error = ErrorResponseHandler(it)
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _uploadScanLogDataOnServer.value =
                    UploadScanDataServerUIState.OnFailure(error.getErrors().message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _uploadScanLogDataOnServer.value = UploadScanDataServerUIState.OnSuccess(it)
            }
        }
    }

    fun deleteScanLogDataFromLocalDB() {
        _deleteScanLogDataFromLocalDB.value = DeleteScanLogDataUIState.IsLoading(true)
        viewModelScope.launch {
            deleteScanLogDataOfflineUseCase.invoke().catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _deleteScanLogDataFromLocalDB.value =
                    DeleteScanLogDataUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _deleteScanLogDataFromLocalDB.value =
                    DeleteScanLogDataUIState.OnSuccess("Data has been deleted successfully!")
            }
        }
    }

    fun getScanReportDataFromLocalDB() {
        _getScanReportDataFromLocalDB.value = GetScanReportDataOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            getScanReportDataOfflineUseCase.invoke().catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _getScanReportDataFromLocalDB.value =
                    GetScanReportDataOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                try {
                    logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                    _getScanReportDataFromLocalDB.value = GetScanReportDataOfflineUIState.OnSuccess(it)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    fun getScanReportTicketListOffline() {
        _getScanReportTicketListFromLocalDB.value =
            GetScanReportTicketListOfflineUIState.IsLoading(true)
        viewModelScope.launch {
            getScanReportTicketListOfflineUseCase.invoke().catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _getScanReportTicketListFromLocalDB.value =
                    GetScanReportTicketListOfflineUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _getScanReportTicketListFromLocalDB.value =
                    GetScanReportTicketListOfflineUIState.OnSuccess(it)
            }
        }
    }

    fun insertTicketTypesOfflineScan(ticketTypeList: ArrayList<InsertTicketTypeListResponse>) {
        _insertTicketTypesOfflineScan.value = InsertTicketTypesOfflineScanUIState.IsLoading(true)
        viewModelScope.launch {
            insertTicketTypesOfflineUseCase.invoke(ticketTypeList).catch {
                logUtil.log(LoginScanVewModel.TAG, "onError${it.message.toString()}")
                _insertTicketTypesOfflineScan.value =
                    InsertTicketTypesOfflineScanUIState.OnFailure(it.message.toString())
            }.collect {
                logUtil.log(LoginScanVewModel.TAG, "onResponse: $it")
                _insertTicketTypesOfflineScan.value =
                    InsertTicketTypesOfflineScanUIState.OnSuccess("Data inserted successfully!")
            }
        }
    }

    companion object {
        val TAG = QrScanViewModel::class.java.simpleName
    }
}