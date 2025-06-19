package com.example.eventapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.databinding.ActivityEditeProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class EditeProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditeProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.back.setOnClickListener {
            finish()
        }

        // Load user name & email from Firestore
        loadUserData()

        // Save changes
        binding.done.setOnClickListener {
            val newName = binding.editname.text.toString().trim()
            val newEmail = binding.editemail.text.toString().trim()

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            user?.let { currentUser ->
                // Update Firestore
                db.collection("users").document(currentUser.uid)
                    .update(
                        mapOf(
                            "name" to newName,
                            "email" to newEmail
                        )
                    )
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                // Update FirebaseAuth displayName
                val profileUpdates = userProfileChangeRequest {
                    displayName = newName
                }
                currentUser.updateProfile(profileUpdates)




            }
        }
    }

    private fun loadUserData() {
        user?.let {
            db.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: ""
                        val email = document.getString("email") ?: ""
                        binding.editname.setText(name)
                        binding.editemail.setText(email)
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
