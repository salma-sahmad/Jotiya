package com.example.l3ezlaapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l3ezlaapp.Adapter.CartAdapter
import com.example.l3ezlaapp.Helper.ChangeNumberItemsListener
import com.example.l3ezlaapp.Helper.ManagmentCart
import com.example.l3ezlaapp.Model.ItemModel
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagmentCart
    private lateinit var userId: String
    private var totalAmount: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        setVariable()
        initCartList()
        calculateCart()

        // Retrieve current user's ID
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Load user-specific cart items from Firestore
//        loadCartFromFirestore(userId)

        binding.checkout.setOnClickListener {
            proceedToCheckout()
        }
    }

    private fun initCartList() {
        binding.cartView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.cartView.adapter =
            CartAdapter(managementCart.getListCart(), this, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            })

        with(binding) {
            emptyTxt.visibility =
                if (managementCart.getListCart().isEmpty()) View.VISIBLE else View.GONE
            scroll.visibility =
                if (managementCart.getListCart().isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun calculateCart() {
        val delivery = 10.0
        val total = managementCart.getTotalFee() + delivery
        val itemTotal = managementCart.getTotalFee()
        totalAmount = total

        with(binding) {
            totalFeeTxt.text = "$itemTotal MAD"
            deliveryTxt.text = "$delivery MAD"
            totalTxt.text = "$total MAD"
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun loadCartFromFirestore(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val cartRef = db.collection("users").document(userId).collection("cart")

        cartRef.get()
            .addOnSuccessListener { documents ->
                val cartItems = mutableListOf<ItemModel>()

                for (document in documents) {
                    val item = document.toObject(ItemModel::class.java)
                    cartItems.add(item)
                }

                // Add each retrieved cart item to your local cart manager
                cartItems.forEach { managementCart.addItemsToCart(it,userId) }

                // Refresh the UI
                initCartList()
                calculateCart()
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }


    private fun proceedToCheckout() {
        val cartItems = managementCart.getListCart()
        val intent = Intent(this@CartActivity, OrdersActivity::class.java).apply {
            putParcelableArrayListExtra("cartItems", ArrayList(cartItems))
            putExtra("totalAmount", totalAmount)
        }
        startActivity(intent)
    }
}
