package com.mtp.scanner.feature_scan_module.feature_login_scan.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.scanner.R
import com.mtp.scanner.common.ZERO
import com.mtp.scanner.common.storage.MyPreferences
import com.mtp.scanner.common.storage.PrefConstants.SCAN_SELECTED_TICKET_TYPES_LIST
import com.mtp.scanner.databinding.LayoutScanSelectTicketTypeBinding
import com.mtp.scanner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

class SelectTicketTypeScanAdapter(
    private val context: Context,
    private val eventTickets: ArrayList<InsertTicketTypeListResponse>?,
    private val selectedTicketName: (ArrayList<String>) -> Unit,
    private val selectedListSize: (Int) -> Unit
) : RecyclerView.Adapter<SelectTicketTypeScanAdapter.ViewHolder>() {

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

        view.tvTicketName.text = eventTickets?.get(position)?.ticketName
        val isSelected = eventTickets?.get(position)?.isSelected ?: false

        if (isSelected) {
            if (!selectedNameList.contains(eventTickets?.get(position)?.ticketName)) {
                selectedNameList.add(eventTickets?.get(position)?.ticketName.toString())
            }
            view.ivChecked.visibility = View.VISIBLE
            view.ivUnChecked.visibility = View.GONE
            view.itemLayout.background =
                context.getDrawable(R.drawable.select_ticket_type_item_purple_design)
        } else {
            view.ivChecked.visibility = View.GONE
            view.ivUnChecked.visibility = View.VISIBLE
            view.itemLayout.background =
                context.getDrawable(R.drawable.select_ticket_type_item_light_purple_design)
        }

        view.itemLayout.setOnClickListener {
            val isSelected = eventTickets?.get(position)?.isSelected

            if (isSelected != null) {
                if (isSelected) {
                    view.ivChecked.visibility = View.GONE
                    view.ivUnChecked.visibility = View.VISIBLE
                    eventTickets?.get(position)?.isSelected = false
                    selectedNameList.remove(eventTickets?.get(position)?.ticketName)
                    view.itemLayout.background =
                        context.getDrawable(R.drawable.select_ticket_type_item_light_purple_design)
                } else {
                    view.ivChecked.visibility = View.VISIBLE
                    view.ivUnChecked.visibility = View.GONE
                    eventTickets?.get(position)?.isSelected = true
                    selectedNameList.add(eventTickets?.get(position)?.ticketName.toString())
                    view.itemLayout.background =
                        context.getDrawable(R.drawable.select_ticket_type_item_purple_design)
                }
            }
            selectedListSize(selectedNameList.size)
            selectedTicketName(selectedNameList)
        }
    }

    override fun getItemCount(): Int {
        return eventTickets?.size ?: ZERO
    }

    class ViewHolder(val binding: LayoutScanSelectTicketTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun selectAll() {
        try {
            if (!eventTickets.isNullOrEmpty()) {
                for (i in eventTickets.indices) {
                    eventTickets[i].isSelected = true
                    if (!selectedNameList.contains(eventTickets[i].ticketName)) {
                        selectedNameList.add(eventTickets[i].ticketName.toString())
                    }
                }
                MyPreferences.putArrayList(SCAN_SELECTED_TICKET_TYPES_LIST, selectedNameList)
                selectedTicketName(selectedNameList)
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun unselectAll() {
        try {
            if (!eventTickets.isNullOrEmpty()) {
                for (i in eventTickets.indices) {
                    eventTickets[i].isSelected = false
                    if (!selectedNameList.contains(eventTickets[i].ticketName)) {
                        selectedNameList.remove(eventTickets[i].ticketName.toString())
                    }
                }
                MyPreferences.putArrayList(SCAN_SELECTED_TICKET_TYPES_LIST, java.util.ArrayList())
                selectedTicketName(java.util.ArrayList())
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}