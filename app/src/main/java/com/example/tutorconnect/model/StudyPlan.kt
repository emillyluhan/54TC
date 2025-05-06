package com.example.tutorconnect.model

import java.text.SimpleDateFormat
import java.util.*

data class StudyPlan(
    val id: String = System.currentTimeMillis().toString(),
    val subject: String,
    val objective: String,
    val startDate: String,
    val endDate: String,
    val timePerWeek: Int = 0,
    val topics: List<String> = emptyList(),
    val tutoringRequired: Boolean = false,
    var isCompleted: Boolean = false,
    var progress: Float = 0f,
    var lastUpdated: Long = System.currentTimeMillis()
) {
    
    fun getFormattedTopics(): String = topics.joinToString("\n• ") { "• $it" }
    
    fun getProgressPercentage(): String {
        return when {
            isCompleted -> "100%"
            progress == 0f -> "0%"
            else -> "${(progress * 100).toInt()}%"
        }
    }
    
    fun markAsCompleted() {
        isCompleted = true
        progress = 1f
        lastUpdated = System.currentTimeMillis()
    }

    fun canUpdateProgress(): Boolean {
        return !isCompleted || progress < 1f
    }
    
    fun updateProgress(newProgress: Float) {
        if (canUpdateProgress()) {
            progress = newProgress.coerceIn(0f, 1f)
            lastUpdated = System.currentTimeMillis()
        }
    }
    
    fun calculateProgress(): Float {
        val totalTopics = topics.size
        if (totalTopics == 0) return 0f
        
        // Aquí podrías implementar un cálculo más complejo basado en:
        // - Fecha actual vs fecha final
        // - Temas completados vs totales
        // - Horas dedicadas vs horas planeadas
        
        // Por ahora, un cálculo simple basado en tiempo:
        val startDate = startDate.toDate() ?: return 0f
        val endDate = endDate.toDate() ?: return 0f
        val currentDate = System.currentTimeMillis()
        
        val totalDays = (endDate.time - startDate.time) / (24 * 60 * 60 * 1000)
        val daysPassed = (currentDate - startDate.time) / (24 * 60 * 60 * 1000)
        
        return (daysPassed.toFloat() / totalDays).coerceIn(0f, 1f)
    }
    
    private fun String.toDate(): Date? {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(this)
        } catch (e: Exception) {
            null
        }
    }
}
