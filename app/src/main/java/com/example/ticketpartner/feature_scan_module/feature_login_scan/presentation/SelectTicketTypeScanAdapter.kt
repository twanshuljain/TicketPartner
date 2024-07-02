package com.example.ticketpartner.feature_scan_module.feature_login_scan.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.R
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.LayoutScanSelectTicketTypeBinding
import com.example.ticketpartner.feature_scan_module.feature_login_scan.domain.model.InsertTicketTypeListResponse

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
            val selectedName = eventTickets?.get(position)?.ticketName
           // Toast.makeText(context, selectedName.toString(), Toast.LENGTH_SHORT).show()

            if (isSelected != null) {
                if (isSelected) {
                    Log.e("TAG", "position: size-->> $position")
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
            Log.e("TAG", "list: size-->> $selectedNameList")
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
                selectedTicketName(selectedNameList)
            }
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("TAG", "list: size-->> $selectedNameList")
    }
}