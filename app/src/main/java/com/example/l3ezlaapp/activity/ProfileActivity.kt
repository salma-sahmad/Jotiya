package com.example.l3ezlaapp.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.databinding.ActivityProfileBinding
import com.example.l3ezlaapp.databinding.ActivityWishlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding


    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var addressTextView: TextView

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText

    private lateinit var updateProfileBtn: Button

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usernameTextView = findViewById(R.id.textView24)
        emailTextView = findViewById(R.id.emailTextView)
        addressTextView = findViewById(R.id.TextViewAddress)

        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        addressEditText = findViewById(R.id.addressEditText)

        updateProfileBtn = findViewById(R.id.profileUpdate)

        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        auth = FirebaseAuth.getInstance()

        updateProfileBtn.setOnClickListener {
            if (isEditing) {
                saveProfileChanges()
            } else {
                enableEditing()
            }
        }

        // Load user data initially
        loadUserData()

        // Set up button click listeners
        binding.backBtn.setOnClickListener { finish() }

        val home = findViewById<ImageView>(R.id.imageView11)
        home.setOnClickListener {
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }


        val like = findViewById<ImageView>(R.id.imageView111)
        like.setOnClickListener {
            val intent = Intent(this@ProfileActivity, WishlistActivity::class.java)
            startActivity(intent)
        }

        val poster = findViewById<ImageView>(R.id.imageView12)
        poster.setOnClickListener {
            val intent = Intent(this@ProfileActivity, PosterActivity::class.java)
            startActivity(intent)
        }

        val cart = findViewById<ImageView>(R.id.imageView163)
        cart.setOnClickListener {
            val intent = Intent(this@ProfileActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val logout = findViewById<ImageView>(R.id.logout)
        logout.setOnClickListener {
            auth.signOut()
            clearLoginState()
            navigateToLoginActivity()
        }

        val order = findViewById<Button>(R.id.orderBtn)
        order.setOnClickListener {
            val intent = Intent(this@ProfileActivity, OrdersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clearLoginState() {
        val editor = sharedPreferences.edit()
        editor.clear() // Clears all stored data
        editor.apply()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }





    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username = document.getString("nomUser")
                        val email = document.getString("emailUser")
                        val address = document.getString("address")

                        // Set text views
                        usernameTextView.text = username
                        emailTextView.text = email
                        addressTextView.text = address

                        // Set edit texts
                        usernameEditText.setText(username)
                        emailEditText.setText(email)
                        addressEditText.setText(address)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun enableEditing() {
        isEditing = true
        updateProfileBtn.text = "Save Profile"

        usernameTextView.visibility = View.GONE
        emailTextView.visibility = View.GONE
        addressTextView.visibility = View.GONE

        usernameEditText.visibility = View.VISIBLE
        emailEditText.visibility = View.VISIBLE
        addressEditText.visibility = View.VISIBLE
    }

    private fun saveProfileChanges() {
        val name = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userUpdates = hashMapOf<String, Any>(
                "nomUser" to name,
                "emailUser" to email,
                "address" to address
            )

            firestore.collection("users").document(userId)
                .update(userUpdates)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Set the text of TextViews with the new values
                    usernameTextView.text = name
                    emailTextView.text = email
                    addressTextView.text = address

                    disableEditing()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun disableEditing() {
        isEditing = false
        updateProfileBtn.text = "Update Profile"

        usernameTextView.text = usernameEditText.text
        emailTextView.text = emailEditText.text
        addressTextView.text = addressEditText.text

        usernameTextView.visibility = View.VISIBLE
        emailTextView.visibility = View.VISIBLE
        addressTextView.visibility = View.VISIBLE

        usernameEditText.visibility = View.GONE
        emailEditText.visibility = View.GONE
        addressEditText.visibility = View.GONE
    }
}
