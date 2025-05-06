package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.tutorconnect.models.SessionStatus

class SelectTutorActivity : AppCompatActivity() {
    // Lista temporal de ejemplo
    private val tutors = listOf(
        "María González - Matemáticas",
        "Juan Pérez - Cálculo",
        "Ana López - Física",
        "Carlos Ruiz - Química"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_tutor)

        val searchInput = findViewById<TextInputEditText>(R.id.searchInput)
        val listView = findViewById<ListView>(R.id.listViewTutors)

        // Configurar adaptador
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tutors)
        listView.adapter = adapter

        // Manejar selección de tutor
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedTutor = tutors[position]
            val intent = Intent(this, TutorRatingActivity::class.java).apply {
                putExtra("TUTOR_NAME", selectedTutor.split(" - ")[0])
                putExtra("SUBJECT", selectedTutor.split(" - ")[1])
                putExtra("DATE", java.time.LocalDate.now().toString())
                putExtra("SESSION_ID", "new_${System.currentTimeMillis()}")
                putExtra("SESSION_STATUS", SessionStatus.COMPLETED.toString())
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
