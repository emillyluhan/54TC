package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class StudentHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        // Configurar clic en la tarjeta de comunicados
        findViewById<CardView>(R.id.cardComunicados).setOnClickListener {
            val intent = Intent(this, AnnouncementsActivity::class.java)
            startActivity(intent)
        }

        // Configurar clic en la tarjeta de evaluación
        findViewById<CardView>(R.id.cardEvaluacion).setOnClickListener {
            val intent = Intent(this, EvaluationActivity::class.java)
            startActivity(intent)
        }

        // Configurar clic en la tarjeta de planificación
        findViewById<CardView>(R.id.cardPlanification).setOnClickListener {
            val intent = Intent(this, StudyPlanListActivity::class.java)
            startActivity(intent)
        }

        // Configurar navegación inferior
        findViewById<ImageView>(R.id.nav_home).setOnClickListener {
            // Ya estamos en home
        }

        findViewById<ImageView>(R.id.nav_search).setOnClickListener {
            val intent = Intent(this, StudentCalendarActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.nav_profile).setOnClickListener {
            val intent = Intent(this, StudentProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
