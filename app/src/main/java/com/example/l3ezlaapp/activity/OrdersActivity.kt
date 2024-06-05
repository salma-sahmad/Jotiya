package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l3ezlaapp.Adapter.OrdersAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityOrdersBinding

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding
    private lateinit var orderAdapter: OrdersAdapter
    private val orderedItems = mutableListOf<ItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the cart items and total amount from the intent
        val cartItems = intent.getParcelableArrayListExtra<ItemModel>("cartItems")
        val totalAmount = intent.getDoubleExtra("totalAmount", 0.0)

        if (cartItems != null) {
            orderedItems.addAll(cartItems)
        }

        // Set up RecyclerView
        orderAdapter = OrdersAdapter(orderedItems)
        binding.recyclerViewOrders.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(this@OrdersActivity)
        }

        // Display total amount or "No orders yet!" message
        if (orderedItems.isEmpty()) {
            binding.totalAmountTextView.visibility = View.GONE
            binding.noOrdersTextView.visibility = View.VISIBLE
        } else {
            binding.totalAmountTextView.text = "Total: $totalAmount MAD"
            binding.totalAmountTextView.visibility = View.VISIBLE
            binding.noOrdersTextView.visibility = View.GONE
        }

        setupNavigation()
        binding.backBtn.setOnClickListener { finish() }

        findViewById<ImageView>(R.id.imageView11).setOnClickListener {
            startActivity(Intent(this@OrdersActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
        }
    }


    private fun setupNavigation() {
        val cartImageView = findViewById<ImageView>(R.id.imageView163)
        val profileImageView = findViewById<ImageView>(R.id.imageView166)
        val posterImageView = findViewById<ImageView>(R.id.imageView12)
        val likeImageView = findViewById<ImageView>(R.id.imageView111)

        if (cartImageView == null) {
            Log.e("MainActivity", "cartImageView is null")
        } else {
            cartImageView.setOnClickListener {
                startActivity(Intent(this@OrdersActivity, CartActivity::class.java))
            }
        }

        if (profileImageView == null) {
            Log.e("MainActivity", "profileImageView is null")
        } else {
            profileImageView.setOnClickListener {
                startActivity(Intent(this@OrdersActivity, ProfileActivity::class.java))
            }
        }

        if (posterImageView == null) {
            Log.e("MainActivity", "posterImageView is null")
        } else {
            posterImageView.setOnClickListener {
                startActivity(Intent(this@OrdersActivity, PosterActivity::class.java))
            }
        }

        if (likeImageView == null) {
            Log.e("MainActivity", "likeImageView is null")
        } else {
            likeImageView.setOnClickListener {
                startActivity(Intent(this@OrdersActivity, WishlistActivity::class.java))
            }
        }
    }
    }
