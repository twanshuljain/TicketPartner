package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.R
import com.example.ticketpartner.common.VERTICAL_DOTS
import com.example.ticketpartner.databinding.ItemScanCheckInLayoutBinding
import com.example.ticketpartner.feature_scan_module.feature_qr_scan.domain.model.Item


class ScanCheckInAdapter(
    private val context: Context,
    private val list: List<Item?>,
    private val checkedOrderId: (ArrayList<Int>) -> Unit
) : RecyclerView.Adapter<ScanCheckInAdapter.ViewHolder>() {
    private var checkedOrderIdList = ArrayList<Int>()

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

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ScanCheckInAdapter.ViewHolder, position: Int) {
        list[position]?.is_checked_in.let {
            if (it!!) {
                holder.binding.CheckBox.visibility = View.INVISIBLE
                holder.binding.tvCheckedIn.visibility = View.VISIBLE
            } else {
                holder.binding.CheckBox.visibility = View.VISIBLE
                holder.binding.tvCheckedIn.visibility = View.GONE
            }
        }
        holder.binding.tvOrderId.text =
            context.getString(R.string.order_id) + VERTICAL_DOTS + list[position]?.order_number

        holder.binding.CheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                list[position]?.order_id?.let { checkedOrderIdList.add(it.toInt()) }
            } else {
                list[position]?.order_number?.let { checkedOrderIdList.remove(it.toInt()) }
            }
            checkedOrderId(checkedOrderIdList)
        }
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }

    class ViewHolder(val binding: ItemScanCheckInLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}
