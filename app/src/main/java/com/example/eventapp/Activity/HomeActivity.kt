package com.example.eventapp.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Go to ProfileActivity
        binding.profile.setOnClickListener {
            val name = binding.proname.text.toString()
            val email = binding.proemail.text.toString()

            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        // Get current user
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "No Name"
                        val email = document.getString("email") ?: "No Email"
                        binding.proname.text = name
                        binding.proemail.text = email
                    }
                }
                .addOnFailureListener {
                    binding.proname.text = "Failed to load"
                    binding.proemail.text = ""
                }
        } else {
            binding.proname.text = "Not logged in"
            binding.proemail.text = ""
        }
    }
}
