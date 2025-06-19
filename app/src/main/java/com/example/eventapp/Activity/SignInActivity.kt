package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.Activity.AdminActivity
import com.example.eventapp.Activity.HomeActivity
import com.example.eventapp.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Sign In button
        binding.signinbtn.setOnClickListener {
            val email = binding.siemail.text.toString().trim()
            val password = binding.SiPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                signInUser(email, password)
            }
        }

        // Navigate to SignUp
        binding.subtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Skip login (optional)
        binding.textView7.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        val userDoc = db.collection("users").document(currentUser.uid)

                        userDoc.get().addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                val isAdmin = doc.getBoolean("admin") == true
                                if (isAdmin) {
                                    // Admin → Go to Admin Panel
                                    startActivity(Intent(this, AdminActivity::class.java))
                                } else {
                                    //  Normal user → Go to Home
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                                finish()
                            } else {
                                // First time login → Create user doc
                                val userData = hashMapOf(
                                    "email" to currentUser.email,
                                    "admin" to false // Default to normal user
                                )
                                userDoc.set(userData).addOnSuccessListener {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
