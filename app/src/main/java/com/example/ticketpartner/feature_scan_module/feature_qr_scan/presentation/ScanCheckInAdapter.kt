package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.databinding.ItemScanCheckInLayoutBinding


class ScanCheckInAdapter(
    private val context: Context,
    private val list: List<String?>
) : RecyclerView.Adapter<ScanCheckInAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScanCheckInAdapter.ViewHolder {
        return ScanCheckInAdapter.ViewHolder(
            ItemScanCheckInLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScanCheckInAdapter.ViewHolder, position: Int) {
      holder.binding.tvOrderId.text = list[position]
    }

    override fun getItemCount(): Int {
      return list.size?:0
    }

    class ViewHolder(val binding: ItemScanCheckInLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}
