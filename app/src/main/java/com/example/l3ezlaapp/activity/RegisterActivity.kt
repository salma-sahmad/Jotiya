package com.example.l3ezlaapp.activity

//import UserDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.databinding.ActivityRegisterBinding
import com.example.l3ezlaapp.database.UserDatabaseHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

        private lateinit var binding: ActivityRegisterBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            registerButton.setOnClickListener {
                val email = email.text.toString()
                val username = username.text.toString()
                val password = password.text.toString()
                val confirmPassword = confirmPassword.text.toString()

                if (password != confirmPassword) {
                    showToast("Passwords do not match.")
                    return@setOnClickListener
                }

                val user = hashMapOf(
                    "email" to email,
                    "username" to username,
                    "password" to password
                )

                db.collection("users").document(username)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@RegisterActivity,
                            "You have signed up successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        showToast("Error registering user: ${e.message}")
                    }
            }

            login.setOnClickListener {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


//    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var databaseHelper: UserDatabaseHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        databaseHelper = UserDatabaseHelper(this)
//
//        binding.registerButton.setOnClickListener {
//            val email = binding.email.text.toString()
//            val username = binding.username.text.toString()
//            val password = binding.password.text.toString()
//            val confirmPassword = binding.confirmPassword.text.toString()
//
//            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//                Toast.makeText(this@RegisterActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
//            } else {
//                if (password == confirmPassword) {
//                    val checkUserEmail = databaseHelper.checkEmail(email)
//
//                    if (!checkUserEmail) {
//                        val insert = databaseHelper.insertData(email, username, password)
//
//                        if (insert) {
//                            Toast.makeText(this@RegisterActivity, "Signup Successfully!", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(applicationContext, LoginActivity::class.java)
//                            startActivity(intent)
//                        } else {
//                            Toast.makeText(this@RegisterActivity, "Signup Failed!", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(this@RegisterActivity, "User already exists! Please login", Toast.LENGTH_SHORT).show()
//                    }
//                } else {
//                    Toast.makeText(this@RegisterActivity, "Passwords do not match!", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        binding.login.setOnClickListener {
//            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}



//    private lateinit var binding: ActivityRegisterBinding
//    private lateinit var dbHelper: UserDatabaseHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        dbHelper = UserDatabaseHelper(this)
//
//        // Handle click on "Already have an account?" text
//        binding.login.setOnClickListener {
//            // Finish the RegisterActivity to go back to the previous activity
//            finish()
//        }
//
//        // Handle click on "Register" button
//        binding.registerButton.setOnClickListener {
//            // Extract user input
//            val email = binding.email.text.toString()
//            val password = binding.password.text.toString()
//            val username = binding.username.text.toString()
//
//            // Check if any field is empty
//            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
//                // Show a message indicating missing fields
//                showToast("Please fill in all fields.")
//                return@setOnClickListener
//            }
//
//            // Add user to the database
//            val isRegistered = dbHelper.addUser(email, password, username)
//
//            // Check if registration was successful
//            if (isRegistered) {
//                // Navigate to the main page
//                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
//                finish() // Finish the RegisterActivity to prevent going back to it
//            } else {
//                // Show a message indicating registration failure
//                showToast("Registration failed. Please try again.")
//            }
//        }
//    }
//
//    private fun showToast(message: String) {
//        // Utility function to show a toast message
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}


/**
 * with firebase noot workiiin!!!!
 */
//    private lateinit var binding: ActivityRegisterBinding
//    private val db = FirebaseFirestore.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.apply {
//            registerButton.setOnClickListener {
//                val email = email.text.toString()
//                val username = username.text.toString()
//                val password = password.text.toString()
//                val confirmPassword = confirmPassword.text.toString()
//
//                if (password != confirmPassword) {
//                    showToast("Passwords do not match.")
//                    return@setOnClickListener
//                }
//
//                val user = hashMapOf(
//                    "email" to email,
//                    "username" to username,
//                    "password" to password
//                )
//
//                db.collection("users").document(username)
//                    .set(user)
//                    .addOnSuccessListener {
//                        Toast.makeText(
//                            this@RegisterActivity,
//                            "You have signed up successfully!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                    .addOnFailureListener { e ->
//                        showToast("Error registering user: ${e.message}")
//                    }
//            }
//
//            login.setOnClickListener {
//                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}
