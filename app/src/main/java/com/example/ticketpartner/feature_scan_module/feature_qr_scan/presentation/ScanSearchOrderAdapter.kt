package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemScanOrderSearchBinding

class ScanSearchOrderAdapter(private val searchResponse: ArrayList<String>,private val isItemClicked: (Boolean) -> Unit) :
    RecyclerView.Adapter<ScanSearchOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScanSearchOrderAdapter.ViewHolder {
        return ViewHolder(
            ItemScanOrderSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScanSearchOrderAdapter.ViewHolder, position: Int) {
        val view = holder.binding
        view.tvName.text = searchResponse[position]
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