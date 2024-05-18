package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize userId
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Set up RecyclerView with WishlistAdapter
//        wishlistAdapter = WishlistAdapter(this, wishlistItems)
//        binding.cartView.layoutManager = LinearLayoutManager(this)
//        binding.cartView.adapter = wishlistAdapter

        // Load wishlist items from Firestore
//        loadWishlistFromFirestore()

        // Set up button click listeners
        binding.backBtn.setOnClickListener { finish() }

        val home = findViewById<ImageView>(R.id.imageView11)
        home.setOnClickListener {
            val intent = Intent(this@WishlistActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        val profileImageView = findViewById<ImageView>(R.id.imageView166)
        profileImageView.setOnClickListener {
            val intent = Intent(this@WishlistActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        val poster = findViewById<ImageView>(R.id.imageView12)
        poster.setOnClickListener {
            val intent = Intent(this@WishlistActivity, PosterActivity::class.java)
            startActivity(intent)
        }

        val cart = findViewById<ImageView>(R.id.imageView163)
        cart.setOnClickListener {
            val intent = Intent(this@WishlistActivity, CartActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun loadWishlistFromFirestore() {
//        val db = FirebaseFirestore.getInstance()
//        val likesRef = db.collection("users").document(userId).collection("likes")
//
//        likesRef.get()
//            .addOnSuccessListener { documents ->
//                val items = mutableListOf<ItemModel>()
//                for (document in documents) {
//                    val item = document.toObject(ItemModel::class.java)
//                    items.add(item)
//                }
//                wishlistAdapter.updateList(items)
//            }
//            .addOnFailureListener { e ->
//                // Handle errors
//            }
//    }
}
