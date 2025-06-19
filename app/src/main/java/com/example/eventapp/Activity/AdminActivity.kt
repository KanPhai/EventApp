package com.example.eventapp.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventapp.adapter.EventAdapter
import com.example.eventapp.databinding.ActivityAdminBinding
import com.example.eventapp.model.Event
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var eventAdapter: EventAdapter
    private val eventList = ArrayList<Event>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadEvents()

        binding.fabAddEvent.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvents() // Refresh events when returning from AddEventActivity
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter(eventList)
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewEvents.adapter = eventAdapter
    }

    private fun loadEvents() {
        db.collection("events")
            .get()
            .addOnSuccessListener { snapshot ->
                eventList.clear()
                Log.d("AdminActivity", "Events found: ${snapshot.size()}")
                for (doc in snapshot) {
                    val event = doc.toObject(Event::class.java)
                    event.id = doc.id
                    eventList.add(event)
                }
                eventAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load events: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("AdminActivity", "Firestore error: ${it.message}")
            }
    }
}
