package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.ticketpartner.scanner.R
import com.mtp.ticketpartner.scanner.common.ZERO
import com.mtp.ticketpartner.scanner.common.storage.MyPreferences
import com.mtp.ticketpartner.scanner.common.storage.PrefConstants
import com.mtp.ticketpartner.scanner.databinding.ItemScanOrderSearchBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.MData

class ScanSearchOrderAdapter(
    private val context: Context,
    private val searchResponse: List<MData?>,
    private val isItemClicked: (MData) -> Unit
) :
    RecyclerView.Adapter<ScanSearchOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemScanOrderSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        view.tvName.text = searchResponse[position]?.name.toString()
        view.tvEmail.text = searchResponse[position]?.email.toString()
        view.tvOrderId.text = context.getString(R.string.order_id)+": "+searchResponse[position]?.order_id.toString()
        view.tvPaymentMethod.text = context.getString(R.string.payment_method)+": "+searchResponse[position]?.payment_type.toString()

        /*if (searchResponse[position]?.is_checkin_allowed == true){
            view.btnViewDetails.isEnabled = true
        } else if (searchResponse[position]?.is_refunded == true) {
            view.btnViewDetails.isEnabled = false
            view.btnViewDetails.text = context.getString(R.string.ticket_refunded)
            view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)
        } else {
            view.btnViewDetails.isEnabled = false
            view.btnViewDetails.text = context.getString(R.string.payment_incomplete)
            view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)
        }*/
        checkInvalid(searchResponse[position], view)
        view.btnViewDetails.setOnClickListener {
            searchResponse[position]?.let { it1 -> isItemClicked(it1) }
        }
    }

    private fun checkInvalid(data: MData?, view: ItemScanOrderSearchBinding){
        val selectedTicketTypeList =
            MyPreferences.getArrayList(PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST)

        val ticketTypeAvailable = data?.ticket_name in selectedTicketTypeList

        if (ticketTypeAvailable){
            if (data?.is_checkin_allowed == true){
                view.btnViewDetails.isEnabled = true
            } else if (data?.is_refunded == true) {
                view.btnViewDetails.isEnabled = false
                view.btnViewDetails.text = context.getString(R.string.ticket_refunded)
                view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)
            } else if(data?.is_partial_payment == true){
                view.btnViewDetails.isEnabled = false
                view.btnViewDetails.text = context.getString(R.string.payment_incomplete)
                view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)
            } else {
                view.btnViewDetails.isEnabled = false
                view.btnViewDetails.text = context.getString(R.string.invalid_ticket)
                view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)
            }
        } else {
            view.btnViewDetails.isEnabled = false
            view.btnViewDetails.text = context.getString(R.string.invalid_ticket)
            view.btnViewDetails.background = context.getDrawable(R.drawable.disable_continue_btn_design)

            // isInvalid = true
           // visibleCheckedInButton(true, getString(R.string.invalid_ticket))
        }
    }

    override fun getItemCount(): Int {
        return searchResponse.size ?: ZERO
    }

    class ViewHolder(val binding: ItemScanOrderSearchBinding) :
        RecyclerView.ViewHolder(binding.root)
}