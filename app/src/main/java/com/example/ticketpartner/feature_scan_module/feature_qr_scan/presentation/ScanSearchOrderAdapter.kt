package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemScanOrderSearchBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.DataList

class ScanSearchOrderAdapter(private val searchResponse: List<DataList?>, private val isItemClicked: (Boolean) -> Unit) :
    RecyclerView.Adapter<ScanSearchOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        return ViewHolder(
            ItemScanOrderSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        view.tvPaymentMethod.text = searchResponse[position]?.payment_type.toString()
        view.btnViewDetails.setOnClickListener {
            isItemClicked(true)
        }
    }

    override fun getItemCount(): Int {
        return searchResponse.size ?: ZERO
    }

    class ViewHolder(val binding: ItemScanOrderSearchBinding) :
        RecyclerView.ViewHolder(binding.root)
}