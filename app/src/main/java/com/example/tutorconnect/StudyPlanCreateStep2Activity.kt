package com.example.tutorconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorconnect.model.StudyPlan
import com.example.tutorconnect.utils.StudyPlanManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StudyPlanCreateStep2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_plan_create_step2)

        val planJson = intent.getStringExtra("plan")
        val type = object : TypeToken<StudyPlan>() {}.type
        val plan = Gson().fromJson<StudyPlan>(planJson, type)
        val timeInput = findViewById<EditText>(R.id.time)
        val topicsInput = findViewById<EditText>(R.id.topics)
        val tutoringInput = findViewById<EditText>(R.id.tutoringRequired)
        val backButton = findViewById<Button>(R.id.buttonBack)
        val nextButton = findViewById<Button>(R.id.buttonFinish)
        val errorText = findViewById<TextView>(R.id.errorText)

        backButton.setOnClickListener {
            finish()
        }

        nextButton.setOnClickListener {
            val timePerWeek = timeInput.text.toString().toIntOrNull() ?: 0
            val topics = topicsInput.text.toString().split("\n").filter { it.isNotBlank() }
            val tutoringRequired = tutoringInput.text.toString().toBoolean()

            if (timePerWeek > 0 && topics.isNotEmpty() && tutoringRequired != null) {
                val updatedPlan = plan.copy(
                    timePerWeek = timePerWeek,
                    topics = topics,
                    tutoringRequired = tutoringRequired
                )

                // Guardar el plan usando StudyPlanManager
                StudyPlanManager.savePlan(this, updatedPlan)
                
                // Crear intent para volver a la lista de planes
                val resultIntent = Intent()
                resultIntent.putExtra("plan", Gson().toJson(updatedPlan))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                errorText.visibility = View.VISIBLE
            }
        }
    }
}