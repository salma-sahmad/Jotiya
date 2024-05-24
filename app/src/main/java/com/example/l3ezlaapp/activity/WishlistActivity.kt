package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l3ezlaapp.Adapter.WishlistAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityWishlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlistItems: MutableList<ItemModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and Adapter
        wishlistAdapter = WishlistAdapter(wishlistItems) { item, isLiked ->
            if (isLiked) addItemToWishlist(item) else removeItemFromWishlist(item)
        }
        binding.LikeView.layoutManager = LinearLayoutManager(this)
        binding.LikeView.adapter = wishlistAdapter

        // Load wishlist items from Firebase
        loadWishlistItems()

        // Set up button click listeners
        binding.backBtn.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.imageView11).setOnClickListener {
            startActivity(Intent(this@WishlistActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }

        findViewById<ImageView>(R.id.imageView166).setOnClickListener {
            startActivity(Intent(this@WishlistActivity, ProfileActivity::class.java))
        }

        findViewById<ImageView>(R.id.imageView12).setOnClickListener {
            startActivity(Intent(this@WishlistActivity, PosterActivity::class.java))
        }

        findViewById<ImageView>(R.id.imageView163).setOnClickListener {
            startActivity(Intent(this@WishlistActivity, CartActivity::class.java))
        }
    }

    private fun loadWishlistItems() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("likes")
            .get()
            .addOnSuccessListener { documents ->
                wishlistItems.clear()
                for (document in documents) {
                    val item = document.toObject(ItemModel::class.java)
                    wishlistItems.add(item)
                }
                wishlistAdapter.notifyDataSetChanged()
                updateEmptyView()
            }
            .addOnFailureListener {
                // Handle any errors here
            }
    }

    private fun updateEmptyView() {
        binding.emptyTxt.visibility = if (wishlistItems.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun addItemToWishlist(item: ItemModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("likes")
            .document(item.id.toString())
            .set(item)
            .addOnSuccessListener {
                if (!wishlistItems.contains(item)) {
                    wishlistItems.add(item)
                }
                wishlistAdapter.notifyDataSetChanged()
                updateEmptyView()
                Toast.makeText(this, "Item added to Wishlist", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add to Wishlist", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeItemFromWishlist(item: ItemModel) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).collection("likes")
            .document(item.id.toString())
            .delete()
            .addOnSuccessListener {
                wishlistItems.remove(item)
                wishlistAdapter.notifyDataSetChanged()
                updateEmptyView()
                Toast.makeText(this, "Item removed from Wishlist", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to remove from Wishlist", Toast.LENGTH_SHORT).show()
            }
    }
}
