package com.example.l3ezlaapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.databinding.ActivityWishlistBinding
//import com.example.l3ezlaapp.Adapter.

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistAdapter: WishlistAdapter
    private val wishlistItems: MutableList<ItemModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView and Adapter
        wishlistAdapter = WishlistAdapter(wishlistItems)
        binding.cartView.apply {
            layoutManager = LinearLayoutManager(this@WishlistActivity)
            adapter = wishlistAdapter
        }

        // Load wishlist items (for demonstration, we'll add some dummy data)
        loadWishlistItems()
    }

    private fun loadWishlistItems() {
        // Add some dummy data
//        wishlistItems.add(LikedItem("1", "Item 1", "$10", "$10", "https://example.com/image1.jpg"))
//        wishlistItems.add(LikedItem("2", "Item 2", "$20", "$20", "https://example.com/image2.jpg"))
//        wishlistItems.add(LikedItem("3", "Item 3", "$30", "$30", "https://example.com/image3.jpg"))

        // Notify the adapter of the data change
        wishlistAdapter.notifyDataSetChanged()
    }
}
