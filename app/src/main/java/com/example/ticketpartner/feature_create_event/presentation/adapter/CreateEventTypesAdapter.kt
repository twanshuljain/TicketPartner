package com.example.ticketpartner.feature_create_event.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.ticketpartner.R

class CreateEventTypesAdapter(context: Context, resource: Int, private val items: ArrayList<String>) :
    ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_drop_down_layout, parent, false)

        val textView: TextView = view.findViewById(R.id.tvSpinner)
        textView.text = items[position]

        return view
    }
}