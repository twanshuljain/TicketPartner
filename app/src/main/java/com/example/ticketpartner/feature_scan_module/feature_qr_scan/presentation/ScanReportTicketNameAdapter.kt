package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.common.FORWARD_SLASH
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemScanReportTicketNameBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.TicketData

class ScanReportTicketNameAdapter(private val ticketName: List<TicketData>) :
    RecyclerView.Adapter<ScanReportTicketNameAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScanReportTicketNameAdapter.ViewHolder {
        return ViewHolder(
            ItemScanReportTicketNameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScanReportTicketNameAdapter.ViewHolder, position: Int) {
        val view = holder.binding
        // val ticket = ticketName.get(ZERO).data?.ticket_data
        view.apply {
            tvTicketName.text = ticketName?.get(ZERO)?.ticket_name
            tvOutOfAmount.text = ticketName?.get(position)?.total_scanned.toString()
            tvTotalAmount.text = FORWARD_SLASH+ticketName?.get(position)?.total_ticket.toString()
        }
    }

    override fun getItemCount(): Int {
        return ticketName.size ?: ZERO
    }

    class ViewHolder(val binding: ItemScanReportTicketNameBinding) :
        RecyclerView.ViewHolder(binding.root)
}

