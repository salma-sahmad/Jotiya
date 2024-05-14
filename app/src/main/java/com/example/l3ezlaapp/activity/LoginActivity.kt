package com.example.l3ezlaapp.activity

//import UserDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.l3ezlaapp.R
import com.example.l3ezlaapp.database.UserDatabaseHelper
import com.example.l3ezlaapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore





class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            loginButton.setOnClickListener {
                if (validateInputs()) {
                    checkUser()
                }
            }

            registerLogin.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Username and password cannot be empty")
            return false
        }
        return true
    }

    private fun checkUser() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val passwordFromDB = document.getString("password")
                        if (passwordFromDB == password) {
                            val emailFromDB = document.getString("email")

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("email", emailFromDB)
                            intent.putExtra("username", username)
                            startActivity(intent)
                            return@addOnSuccessListener
                        } else {
                            showToast("Invalid password")
                        }
                    }
                } else {
                    showToast("User does not exist")
                }
            }
            .addOnFailureListener { exception ->
                showToast("Error checking user: ${exception.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}



//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var databaseHelper: UserDatabaseHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        databaseHelper = UserDatabaseHelper(this)
//
//        binding.loginButton.setOnClickListener {
//            val userInput = binding.username.text.toString() // User can input email or username
//            val password = binding.password.text.toString()
//
//            if (userInput.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
//            } else {
//                val isLoggedIn = if (userInput.contains("@")) {
//                    // If the user input contains "@", consider it as email
//                    databaseHelper.checkEmailPassword(userInput, password)
//                } else {
//                    // Otherwise, consider it as username
//                    databaseHelper.checkCredentials(userInput, password)
//                }
//
//                if (isLoggedIn) {
//                    Toast.makeText(this@LoginActivity, "Login Successfully!", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(applicationContext, MainActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        binding.registerLogin.setOnClickListener {
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }
//}


/**
 * classsique way without storage
 */
//    private lateinit var binding: ActivityLoginBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Find the TextView containing the "Don't have an account?" text
//        val textViewRegister = findViewById<TextView>(R.id.registerLogin)
//        val loginButton = findViewById<Button>(R.id.loginButton)
//        val username = findViewById<EditText>(R.id.username)
//        val password = findViewById<EditText>(R.id.password)
//        // Create a SpannableString from the text
//        val spannableString = SpannableString(textViewRegister.text)
//
//        // Create a ClickableSpan for the "Register" text
//        val clickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                // Start the RegisterActivity when the text is clicked
//                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//            }
//        }
//
//        // Set the ClickableSpan to the "Register" text
//        spannableString.setSpan(
//            clickableSpan,
//            23,
//            spannableString.length,
//            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        // Set the modified SpannableString to the TextView
//        textViewRegister.text = spannableString
//
//        // Enable clickable links in the TextView
//        textViewRegister.movementMethod = LinkMovementMethod.getInstance()
//
//
//        // Set click listener for login button
//        loginButton.setOnClickListener {
//            // Validate input fields
//            val emailOrUsername = username.text.toString()
//            val password = password.text.toString()
//
//            if (validateInputs(emailOrUsername, password)) {
//                // Check user credentials
//                if (userExists(emailOrUsername, password)) {
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
////                } else if(isAdmin(emailOrUsername,password)){
////                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                }else {
//                    showToast("You don't have an account !")
//                }
//            }else{
//                showToast("Invalid username or password !")
//            }
//        }
//
//
//
//    }
//
//    private fun validateInputs(emailOrUsername: String, Userpassword: String): Boolean {
//        // Implement input validation logic
//        // For example, check if email/username and password are not empty
//        if (emailOrUsername.isEmpty() || Userpassword.isEmpty()) {
//            showToast("Please enter email/username and password")
//            return false
//        }else {
//            if(Userpassword.length < 6)
//                return false
//        }
//        return true
//    }
//
//    private fun userExists(emailOrUsername: String, password: String): Boolean {
//        // Use UserDatabaseHelper to check if the user exists
//        val dbHelper = UserDatabaseHelper(this)
//        return dbHelper.checkUser(emailOrUsername, password)
//    }
//
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}

/**
 * with firebase noot wooorkiiingngngngng
 */

//private lateinit var binding: ActivityLoginBinding
//    private val db = FirebaseFirestore.getInstance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        binding.apply {
//            loginButton.setOnClickListener {
//                if (validateInputs()) {
//                    checkUser()
//                }
//            }
//
//            registerLogin.setOnClickListener {
//                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun validateInputs(): Boolean {
//        val username = binding.username.text.toString().trim()
//        val password = binding.password.text.toString().trim()
//
//        if (username.isEmpty() || password.isEmpty()) {
//            showToast("Username and password cannot be empty")
//            return false
//        }
//        return true
//    }
//
//    private fun checkUser() {
//        val username = binding.username.text.toString().trim()
//        val password = binding.password.text.toString().trim()
//
//        db.collection("users")
//            .whereEqualTo("username", username)
//            .get()
//            .addOnSuccessListener { documents ->
//                if (!documents.isEmpty) {
//                    for (document in documents) {
//                        val passwordFromDB = document.getString("password")
//                        if (passwordFromDB == password) {
//                            val emailFromDB = document.getString("email")
//
//                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                            intent.putExtra("email", emailFromDB)
//                            intent.putExtra("username", username)
//                            startActivity(intent)
//                            return@addOnSuccessListener
//                        } else {
//                            showToast("Invalid password")
//                        }
//                    }
//                } else {
//                    showToast("User does not exist")
//                }
//            }
//            .addOnFailureListener { exception ->
//                showToast("Error checking user: ${exception.message}")
//            }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//}
