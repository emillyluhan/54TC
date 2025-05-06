package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorconnect.adapters.TutorSessionAdapter
import com.example.tutorconnect.models.SessionStatus
import com.example.tutorconnect.models.TutorRating
import com.example.tutorconnect.models.TutorSession
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EvaluationActivity : AppCompatActivity() {
    companion object {
        private const val RATING_REQUEST_CODE = 100
        private const val SELECT_TUTOR_REQUEST_CODE = 101
    }
    private lateinit var adapter: TutorSessionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout

    // Lista temporal de ejemplo con calificaciones
    private val sessions = mutableListOf(
        TutorSession(
            id = "1",
            tutorName = "María González",
            subject = "Ecuaciones Lineales",
            date = "12/03/2024",
            status = SessionStatus.COMPLETED
        ),
        TutorSession(
            id = "2",
            tutorName = "Juan Pérez",
            subject = "Cálculo Diferencial",
            date = "15/03/2024",
            status = SessionStatus.RATED,
            rating = TutorRating(
                sessionId = "2",
                tutorName = "Juan Pérez",
                subject = "Cálculo Diferencial",
                date = "15/03/2024",
                rating = 5,
                feedback = "Excelente explicación de los conceptos difíciles. Muy paciente y claro."
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)

        initializeViews()
        setupRecyclerView()
        setupFab()
        updateEmptyState()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerViewTutors)
        emptyState = findViewById(R.id.emptyState)
    }

    private fun setupRecyclerView() {
        adapter = TutorSessionAdapter(sessions) { session ->
            when (session.status) {
                SessionStatus.COMPLETED -> navigateToRating(session)
                SessionStatus.RATED -> navigateToViewRating(session)
                else -> { /* No action for pending sessions */ }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAddRating).setOnClickListener {
            startActivityForResult(Intent(this, SelectTutorActivity::class.java), SELECT_TUTOR_REQUEST_CODE)
        }
    }

    private fun navigateToRating(session: TutorSession) {
        val intent = Intent(this, TutorRatingActivity::class.java).apply {
            putExtra("TUTOR_NAME", session.tutorName)
            putExtra("SUBJECT", session.subject)
            putExtra("DATE", session.date)
            putExtra("SESSION_ID", session.id)
            putExtra("SESSION_STATUS", session.status.toString())
        }
        startActivityForResult(intent, RATING_REQUEST_CODE)
    }

    private fun navigateToViewRating(session: TutorSession) {
        val rating = session.rating
        if (rating != null) {
            val intent = Intent(this, TutorRatingActivity::class.java).apply {
                putExtra("TUTOR_NAME", session.tutorName)
                putExtra("SUBJECT", session.subject)
                putExtra("DATE", session.date)
                putExtra("SESSION_ID", session.id)
                putExtra("SESSION_STATUS", session.status.toString())
                putExtra("RATING", rating.rating)
                putExtra("FEEDBACK", rating.feedback)
            }
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                RATING_REQUEST_CODE -> handleRatingResult(data)
                SELECT_TUTOR_REQUEST_CODE -> handleNewTutorSelection(data)
            }
        }
    }

    private fun handleNewTutorSelection(data: Intent) {
        val sessionId = data.getStringExtra("SESSION_ID") ?: return
        val tutorName = data.getStringExtra("TUTOR_NAME") ?: return
        val subject = data.getStringExtra("SUBJECT") ?: return
        val date = data.getStringExtra("DATE") ?: return
        val status = data.getStringExtra("SESSION_STATUS")?.let { SessionStatus.valueOf(it) } ?: return

        // Crear nueva sesión
        val newSession = TutorSession(
            id = sessionId,
            tutorName = tutorName,
            subject = subject,
            date = date,
            status = status
        )

        // Agregar a la lista y actualizar UI
        sessions.add(newSession)
        adapter.updateSessions(sessions)
        updateEmptyState()

        // Iniciar actividad de calificación
        navigateToRating(newSession)
    }

    private fun handleRatingResult(data: Intent) {
        val sessionId = data.getStringExtra("SESSION_ID") ?: return
        val rating = data.getIntExtra("RATING", 0)
        val feedback = data.getStringExtra("FEEDBACK") ?: ""
        
        // Encontrar y actualizar la sesión calificada
        val sessionIndex = sessions.indexOfFirst { it.id == sessionId }
        if (sessionIndex != -1) {
            val session = sessions[sessionIndex]
            sessions[sessionIndex] = session.copy(
                status = SessionStatus.RATED,
                rating = TutorRating(
                    sessionId = sessionId,
                    tutorName = session.tutorName,
                    subject = session.subject,
                    date = session.date,
                    rating = rating,
                    feedback = feedback
                )
            )
            
            // Actualizar el adaptador
            adapter.updateSessions(sessions)
            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        if (sessions.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        // Aquí se debería actualizar la lista de sesiones desde la base de datos
        adapter.updateSessions(sessions)
        updateEmptyState()
    }
}
