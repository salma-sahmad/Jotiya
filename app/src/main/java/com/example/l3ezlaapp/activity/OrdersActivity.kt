package com.example.l3ezlaapp.activity

import android.os.Bundle
import android.view.View
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

        binding.backBtn.setOnClickListener { finish() }
    }
}
