package com.example.tutorconnect.models

enum class SessionStatus {
    PENDING,    // Sesión programada pero no realizada
    COMPLETED,  // Sesión realizada pero no evaluada
    RATED       // Sesión evaluada
}
