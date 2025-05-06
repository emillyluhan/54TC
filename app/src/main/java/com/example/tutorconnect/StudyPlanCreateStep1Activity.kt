package com.example.tutorconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tutorconnect.model.StudyPlan
import com.example.tutorconnect.utils.StudyPlanManager
import com.google.gson.Gson
import android.view.View

class StudyPlanCreateStep1Activity : AppCompatActivity() {
    companion object {
        private const val CREATE_PLAN_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_plan_create_step1)
        
        // Configurar la toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Crear plan de estudios"
            setDisplayHomeAsUpEnabled(true)
        }

        val nextButton = findViewById<Button>(R.id.buttonNext)
        val subject = findViewById<EditText>(R.id.subject)
        val objective = findViewById<EditText>(R.id.objective)
        val startDate = findViewById<EditText>(R.id.startDate)
        val endDate = findViewById<EditText>(R.id.endDate)
        val errorText = findViewById<TextView>(R.id.errorText)

        nextButton.setOnClickListener {
            val subjectText = subject.text.toString()
            val objectiveText = objective.text.toString()
            val startDateText = startDate.text.toString()
            val endDateText = endDate.text.toString()

            if (subjectText.isNotEmpty() && objectiveText.isNotEmpty() && startDateText.isNotEmpty() && endDateText.isNotEmpty()) {
                val plan = StudyPlan(
                    subject = subjectText,
                    objective = objectiveText,
                    startDate = startDateText,
                    endDate = endDateText,
                    timePerWeek = 0,
                    preferredDays = ""
                )

                val intent = Intent(this, StudyPlanCreateStep2Activity::class.java)
                intent.putExtra("plan", Gson().toJson(plan))
                startActivityForResult(intent, CREATE_PLAN_REQUEST)
            } else {
                errorText.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CREATE_PLAN_REQUEST) {
            data?.getStringExtra("plan")?.let { planJson ->
                val updatedPlan = Gson().fromJson(planJson, StudyPlan::class.java)
                StudyPlanManager.savePlan(this, updatedPlan)
                finish()
            }
        }
    }
}