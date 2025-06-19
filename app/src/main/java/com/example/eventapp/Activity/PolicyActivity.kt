package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.databinding.ActivityPolicyBinding

class PolicyActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPolicyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backicon.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        val privacyTextView = findViewById<TextView>(R.id.privacyText)
        privacyTextView.text = Html.fromHtml(getString(R.string.privacy_policy), Html.FROM_HTML_MODE_LEGACY)




    }
}