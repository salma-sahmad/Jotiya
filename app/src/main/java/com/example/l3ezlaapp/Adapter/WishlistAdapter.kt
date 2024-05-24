package com.example.l3ezlaapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.activity.DetailActivity
import com.example.l3ezlaapp.databinding.ViewholderLikedBinding

class WishlistAdapter(
    private val items: MutableList<ItemModel>,
    private val onLikeChanged: (ItemModel, Boolean) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderLikedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val binding = ViewholderLikedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "${item.price} MAD"

        val requestOptions = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(requestOptions)
            .into(holder.binding.pic)

        holder.binding.likeCheckBox.setOnCheckedChangeListener(null)
        holder.binding.likeCheckBox.isChecked = item.isLiked
        holder.binding.likeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            item.isLiked = isChecked
            onLikeChanged(item, isChecked)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", item)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
