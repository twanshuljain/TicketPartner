package com.mtp.scanner.feature_scan_module.feature_qr_scan.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.scanner.common.FORWARD_SLASH
import com.mtp.scanner.common.ZERO
import com.mtp.scanner.databinding.ItemScanReportTicketNameBinding
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.TicketDataList

class ScanReportTicketNameAdapter(private val res: List<TicketDataList?>?) :
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
            tvTicketName.text = res?.get(position)?.ticket_name.toString()
            tvOutOfAmount.text =  res?.get(position)?.total_scanned.toString()
            //tvTotalAmount.text = FORWARD_SLASH + res?.get(position)?.total_ticket.toString()
            progressBar.progress = res?.get(position)?.total_scanned!!?: ZERO
        }
    }

    override fun getItemCount(): Int {
        return res?.size ?: ZERO
    }

    class ViewHolder(val binding: ItemScanReportTicketNameBinding) :
        RecyclerView.ViewHolder(binding.root)
}

