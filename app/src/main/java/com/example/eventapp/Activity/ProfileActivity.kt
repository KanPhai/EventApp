package com.example.eventapp.Activity
import android.content.Intent
import android.os.Bundle
import android.security.identity.CipherSuiteNotSupportedException
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.MainActivity
import com.example.eventapp.SignInActivity
import com.example.eventapp.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val name = intent.getStringExtra("name") ?: "No Name"
        val email = intent.getStringExtra("email") ?: "No Email"

        binding.username.text = name
        binding.useremail.text = email

        binding.back.setOnClickListener{
            var intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }
        binding.editprofile.setOnClickListener{
            var intent = Intent(this,EditeProfileActivity::class.java)
            startActivity(intent)
        }
        binding.detail.setOnClickListener{
            var intent = Intent(this,DetailsActivity::class.java)
            startActivity(intent)
        }
        binding.deleteacc.setOnClickListener{
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener{
            var intent = Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }


    }
}