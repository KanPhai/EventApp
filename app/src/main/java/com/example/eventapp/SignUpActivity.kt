package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
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
        // Privacy
        binding.policy.setOnClickListener {
            startActivity(Intent(this, PolicyActivity::class.java))
            finish()
        }

        // Sign up button
        binding.signupbtn.setOnClickListener {
            val fullName = binding.suname.text.toString().trim()
            val email = binding.suemail.text.toString().trim()
            val password = binding.supassword.text.toString().trim()
            val confirmPassword = binding.suconfirmpass.text.toString().trim()
            val checked = binding.checkBox.isChecked

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

            // Firebase sign-up
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java)) // change to your home screen
                        finish()
                    } else {
                        Toast.makeText(this, "Sign-up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }


        }
    }
}
