package com.example.eventapp.Activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.databinding.ActivityAddEventBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEventBinding
    private var imageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Modern image picker
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        binding.ivEventImage.setImageURI(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Select image from gallery
        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Save event to Firestore
        binding.btnSaveEvent.setOnClickListener {
            saveEventToFirestore()
        }
    }

    private fun saveEventToFirestore() {
        val name = binding.etEventName.text.toString().trim()
        val date = binding.etEventDate.text.toString().trim()
        val time = binding.etEventTime.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val totalSeats = binding.etTotalSeats.text.toString().toIntOrNull() ?: 0

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri != null) {
            // Upload image to Firebase Storage
            val fileName = UUID.randomUUID().toString()
            val imageRef = storage.reference.child("event_images/$fileName")

            imageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        uploadEventToFirestore(name, date, time, location, totalSeats, uri.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Save event without image
            uploadEventToFirestore(name, date, time, location, totalSeats, "")
        }
    }

    private fun uploadEventToFirestore(
        name: String,
        date: String,
        time: String,
        location: String,
        totalSeats: Int,
        imageUrl: String
    ) {
        val eventData = hashMapOf(
            "name" to name,
            "date" to date,
            "time" to time,
            "location" to location,
            "totalSeats" to totalSeats,
            "availableSeats" to totalSeats,
            "imageUrl" to imageUrl
        )

        db.collection("events")
            .add(eventData)
            .addOnSuccessListener {
                Toast.makeText(this, "Event added successfully", Toast.LENGTH_SHORT).show()
                finish() // Go back to AdminHomeActivity
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add event: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
