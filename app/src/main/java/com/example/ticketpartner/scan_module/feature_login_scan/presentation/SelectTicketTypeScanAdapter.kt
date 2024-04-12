package com.example.ticketpartner.scan_module.feature_login_scan.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.LayoutScanSelectTicketTypeBinding
import com.example.ticketpartner.scan_module.feature_login_scan.domain.model.EventTicket

class SelectTicketTypeScanAdapter(
    private val context: Context,
    private val eventTickets: List<EventTicket?>?,
    private val selectedTicketName: (ArrayList<String>) -> Unit
) :
    RecyclerView.Adapter<SelectTicketTypeScanAdapter.ViewHolder>() {
    private var selectedNameList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutScanSelectTicketTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        view.tvTicketType.text = eventTickets?.get(position)?.ticket_type

        view.itemLayout.setOnClickListener {
            val isSelected = eventTickets?.get(position)?.isSelected
            if (isSelected != null) {
                if (isSelected) {
                    selectedNameList.remove(eventTickets?.get(position)?.ticket_name.toString())
                    eventTickets?.get(position)?.isSelected = false
                    view.itemLayout.background =
                        context.getDrawable(R.drawable.select_ticket_type_item_light_purple_design)
                } else {
                    selectedNameList.add(eventTickets?.get(position)?.ticket_name.toString())
                    eventTickets?.get(position)?.isSelected = true
                    view.itemLayout.background =
                        context.getDrawable(R.drawable.select_ticket_type_item_purple_design)
                }
                selectedTicketName(selectedNameList)
            }


            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return eventTickets?.size ?: ZERO
    }

    class ViewHolder(val binding: LayoutScanSelectTicketTypeBinding) :
        RecyclerView.ViewHolder(binding.root)
}