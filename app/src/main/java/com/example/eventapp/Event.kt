package com.example.eventapp.model

data class Event(
    var id: String = "",
    var name: String = "",
    var date: String = "",
    var time: String = "",
    var location: String = "",
    var totalSeats: Int = 0,
    var availableSeats: Int = 0,
    var imageUrl: String = ""
)
