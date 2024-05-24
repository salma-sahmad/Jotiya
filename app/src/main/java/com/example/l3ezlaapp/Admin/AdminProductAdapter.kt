package com.example.l3ezlaapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ViewholderAdminProductBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminProductAdapter(
    private val items: MutableList<ItemModel>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.ViewHolder>() {

    private var context: Context? = null

    class ViewHolder(val binding: ViewholderAdminProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderAdminProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "${item.price} MAD"

        // Check if picUrl is not empty before trying to access the first element
        if (item.picUrl.isNotEmpty()) {
            val requestOptions = RequestOptions().transform(CenterCrop())
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0])
                .apply(requestOptions)
                .into(holder.binding.pic)
        } else {
            // Handle the case where picUrl is empty
            holder.binding.pic.setImageResource(R.drawable.grey_bg) // Use a placeholder image
        }

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(item.id)
        }
    }

    override fun getItemCount(): Int = items.size
}
