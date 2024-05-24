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
import com.example.l3ezlaapp.databinding.ViewholderRecommendedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PopularAdapter(val items: MutableList<ItemModel>) :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {
    private var context: Context? = null

    class ViewHolder(val binding: ViewholderRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
        context = parent.context
        val binding =
            ViewholderRecommendedBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
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
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnCheckedChangeListener
            val userCollection = FirebaseFirestore.getInstance().collection("users").document(userId).collection("likes")

            if (isChecked) {
                userCollection.document(item.id.toString()).set(item)
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Item added to Wishlist", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show()
                    }
            } else {
                userCollection.document(item.id.toString()).delete()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "Item removed from Wishlist", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(holder.itemView.context, "Failed to remove from Wishlist", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("object", items[position])
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = items.size
}
