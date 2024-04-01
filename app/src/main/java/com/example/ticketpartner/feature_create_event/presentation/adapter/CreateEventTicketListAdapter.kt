package com.example.ticketpartner.feature_create_event.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketpartner.common.EMPTY_STRING
import com.example.ticketpartner.common.FORWARD_SLASH
import com.example.ticketpartner.common.ONE
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemTicketListCreateEventBinding
import com.example.ticketpartner.feature_create_event.domain.model.CreateEventTicketListResponse

class CreateEventTicketListAdapter(
    private val context: Context,
    private val response: List<CreateEventTicketListResponse>,
    private val onMinusClick: (Int) -> Unit,
    private val onPlusClick: (Int) -> Unit,
    private val onEllipsisClick: () -> Unit
) :
    RecyclerView.Adapter<CreateEventTicketListAdapter.ViewHolder>() {
    private var count = ZERO

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateEventTicketListAdapter.ViewHolder {
        return CreateEventTicketListAdapter.ViewHolder(
            ItemTicketListCreateEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CreateEventTicketListAdapter.ViewHolder, position: Int) {
        val view = holder.binding
        val data = response[ZERO].data?.get(position)

        view.tvTitleName.text =
            data?.ticket_name ?: EMPTY_STRING

        view.tvCount.text = count.toString()
        view.tvTicketType.text = data?.ticket_type
        view.tvSoldOnCount.text =
            ":   "+data?.sold_ticket.toString() + FORWARD_SLASH + data?.ticket_quantity.toString()

        view.ivMinus.setOnClickListener {
            if (count != ZERO) {
                count -= ONE
                holder.binding.tvCount.text = count.toString()
            }
        }

        view.ivPlus.setOnClickListener {
            count += ONE
            holder.binding.tvCount.text = count.toString()
        }

        view.ivEllipsis.setOnClickListener {
            onEllipsisClick()
        }
    }

    override fun getItemCount(): Int {
        return response[ZERO].data?.size ?: ZERO
    }

    class ViewHolder(val binding: ItemTicketListCreateEventBinding) :
        RecyclerView.ViewHolder(binding.root)
}