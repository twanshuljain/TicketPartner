package com.example.ticketpartner.feature_create_event.presentation.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ticketpartner.common.ZERO
import com.example.ticketpartner.databinding.ItemImageListCreateEventBinding

class AddMoreImagesAdapter(private val context: Context, private val imageList: List<Bitmap>, private val onDeleteClick:(Int)-> Unit) :
    RecyclerView.Adapter<AddMoreImagesAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageListCreateEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])
            .into(holder.binding.ivImage)

        holder.binding.ivDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size ?: ZERO
    }

    class ImageViewHolder(val binding: ItemImageListCreateEventBinding) :
        RecyclerView.ViewHolder(binding.root)
}