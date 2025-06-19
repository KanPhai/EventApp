package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.Activity.HomeActivity
import com.example.eventapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Back button
        binding.backicon.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Sign in text
        binding.signinbtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Privacy policy
        binding.policy.setOnClickListener {
            startActivity(Intent(this, PolicyActivity::class.java))
            finish()
        }

        // Sign-up button
        binding.signupbtn.setOnClickListener {
            val fullName = binding.suname.text.toString().trim()
            val email = binding.suemail.text.toString().trim()
            val password = binding.supassword.text.toString().trim()
            val confirmPassword = binding.suconfirmpass.text.toString().trim()
            val checked = binding.checkBox.isChecked

            // Validate fields
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checked) {
                Toast.makeText(this, "Please accept the privacy policy.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Auth: Create user
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            // Save user data to Firestore
                            val userData = hashMapOf(
                                "name" to fullName,
                                "email" to email
                            )

                            db.collection("users")
                                .document(user.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
