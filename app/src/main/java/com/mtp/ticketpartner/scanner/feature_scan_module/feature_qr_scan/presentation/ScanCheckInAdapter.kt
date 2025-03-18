package com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mtp.scanner.R
import com.mtp.ticketpartner.scanner.common.VERTICAL_DOTS
import com.mtp.scanner.databinding.ItemScanCheckInLayoutBinding
import com.mtp.ticketpartner.scanner.feature_scan_module.feature_qr_scan.domain.model.Item


class ScanCheckInAdapter(
    private val context: Context,
    private val list: List<Item?>,
    private val checkedOrderId: (ArrayList<Long>) -> Unit,
    private val position: (Int) -> Unit
) : RecyclerView.Adapter<ScanCheckInAdapter.ViewHolder>() {
    private var checkedOrderIdList = ArrayList<Long>()
    private var checkedItemSize = ArrayList<Int>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemScanCheckInLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        item?.is_checked_in?.let {
            if (it) {
                list[position]?.order_id?.let { it1 -> checkedItemSize.add(it1) }
                holder.binding.CheckBox.visibility = View.INVISIBLE
                holder.binding.tvCheckedIn.visibility = View.VISIBLE
            } else {
                holder.binding.CheckBox.visibility = View.VISIBLE
                holder.binding.tvCheckedIn.visibility = View.GONE
            }
        }
        holder.binding.tvOrderId.text =
            context.getString(R.string.order_id) + VERTICAL_DOTS + list[position]?.order_id


        holder.binding.CheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (item?.stripe_obj?.is_ticket_download == true) {
                if (isChecked) {
                    item?.order_id?.let { checkedOrderIdList.add(it.toLong()) }
                } else {
                    item?.order_id?.let { checkedOrderIdList.remove(it.toLong()) }
                }
                position(position)
                checkedOrderId(checkedOrderIdList)
            } else {
                holder.binding.CheckBox.isChecked = false
                Toast.makeText(context, context.getString(R.string.your_payment_has_not_been_completed), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }

    class ViewHolder(val binding: ItemScanCheckInLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}
