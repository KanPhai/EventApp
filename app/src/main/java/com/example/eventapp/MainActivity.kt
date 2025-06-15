package com.example.eventapp

import com.example.eventapp.Adapter.ImageSliderAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.eventapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageSliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val images = listOf(
            R.drawable.image1,
            R.drawable.image2,

        )

        imageAdapter = ImageSliderAdapter(images)
        binding.viewpager.adapter = imageAdapter
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.getstartbtn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }
}