package com.mtp.scanner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.scanner.R
import com.mtp.scanner.common.ZERO
import com.mtp.scanner.databinding.ItemScanOrderSearchBinding
import com.mtp.scanner.feature_scan_module.feature_qr_scan.domain.model.MData

class ScanSearchOrderAdapter(
   private val context: Context,
    private val searchResponse: List<MData?>,
    private val isItemClicked: (MData) -> Unit
) :
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScanSearchOrderAdapter.ViewHolder, position: Int) {
        val view = holder.binding
        view.tvName.text = searchResponse[position]?.name.toString()
        view.tvEmail.text = searchResponse[position]?.email.toString()
        view.tvOrderId.text = context.getString(R.string.order_id)+": "+searchResponse[position]?.order_id.toString()
        view.tvPaymentMethod.text = context.getString(R.string.payment_method)+": "+searchResponse[position]?.payment_type.toString()
        view.btnViewDetails.setOnClickListener {
            searchResponse[position]?.let { it1 -> isItemClicked(it1) }
        }
    }

    override fun getItemCount(): Int {
        return searchResponse.size ?: ZERO
    }

    class ViewHolder(val binding: ItemScanOrderSearchBinding) :
        RecyclerView.ViewHolder(binding.root)
}