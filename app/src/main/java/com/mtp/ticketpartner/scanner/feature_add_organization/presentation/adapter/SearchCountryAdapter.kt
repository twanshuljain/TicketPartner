package com.mtp.ticketpartner.scanner.feature_add_organization.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtp.ticketpartner.scanner.databinding.ItemCountrySearchBinding
import com.mtp.ticketpartner.scanner.feature_add_organization.domain.model.SearchItem

class SearchCountryAdapter(
    private val searchList: List<SearchItem?>,
    private val onEventClick: (Int, String) -> Unit,
) :
    RecyclerView.Adapter<SearchCountryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCountrySearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCountryName.text = searchList[position]?.country_name
        val countryId: Int? = searchList[position]?.id
        val countryName = searchList[position]?.country_name.toString()
        holder.binding.itemLayout.setOnClickListener {
            countryId?.let {
                onEventClick(it, countryName)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchList.size ?: 0
    }

    class ViewHolder(val binding: ItemCountrySearchBinding) :
        RecyclerView.ViewHolder(binding.root)
}