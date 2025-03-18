package com.mtp.ticketpartner.scanner.feature_create_event.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.ticketpartner.scanner.common.EMPTY_STRING
import com.mtp.ticketpartner.scanner.common.FORWARD_SLASH
import com.mtp.ticketpartner.scanner.common.ONE
import com.mtp.ticketpartner.scanner.common.ZERO
import com.mtp.scanner.databinding.ItemTicketListCreateEventBinding
import com.mtp.ticketpartner.scanner.feature_create_event.domain.model.CreateEventTicketListResponse

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
    ): ViewHolder {
        return ViewHolder(
            ItemTicketListCreateEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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