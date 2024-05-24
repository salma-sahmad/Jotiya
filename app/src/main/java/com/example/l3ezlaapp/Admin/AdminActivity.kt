package com.example.l3ezlaapp.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.l3ezlaapp.Adapter.AdminProductAdapter
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: AdminProductAdapter
    private val products = mutableListOf<ItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewProducts)
        productAdapter = AdminProductAdapter(products) { productId -> deleteProduct(productId.toInt()) }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadProducts()
    }

    private fun loadProducts() {
        firestore.collection("products").get()
            .addOnSuccessListener { result ->
                products.clear()
                for (document in result) {
                    val product = document.toObject(ItemModel::class.java).apply {
                        id = document.id.toIntOrNull() ?: 0
                    }
                    products.add(product)
                }
                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load products: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteProduct(productId: Int) {
        firestore.collection("products").document(productId.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                loadProducts()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error deleting product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
