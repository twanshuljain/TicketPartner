package com.example.ticketpartner.feature_scan_module.feature_qr_scan.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemScanOrderSearchBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.SearchData

class ScanSearchOrderOfflineAdapter(
    private val context: Context,
    private val searchResponse: List<SearchData?>,
    private val isItemClicked: (SearchData) -> Unit
):
RecyclerView.Adapter<ScanSearchOrderOfflineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScanSearchOrderOfflineAdapter.ViewHolder {
        return ViewHolder(
            ItemScanOrderSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScanSearchOrderOfflineAdapter.ViewHolder, position: Int) {
        val view = holder.binding
        view.tvName.text = searchResponse[position]?.name.toString()
        view.tvEmail.text = searchResponse[position]?.email.toString()
        view.tvOrderId.text = context.getString(R.string.order_id)+": "+searchResponse[position]?.order_number.toString()
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