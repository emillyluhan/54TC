package com.example.tutorconnect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorconnect.model.StudyPlan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.tutorconnect.StudyPlanAdapter
import com.example.tutorconnect.utils.StudyPlanManager

class StudyPlanListActivity : AppCompatActivity() {
    companion object {
        private const val CREATE_PLAN_REQUEST = 1
        private const val DETAIL_PLAN_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_plan_list)

        // Configurar botón de navegación
        findViewById<Button>(R.id.btnBack)?.setOnClickListener {
            startActivity(Intent(this, StudentHomeActivity::class.java))
            finish()
        }

        // Verificar si hay un nuevo plan
        handleNewPlan()

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StudyPlanAdapter(getPlans(), this)

        // Configurar TextView de "No hay planes"
        val noPlansText = findViewById<TextView>(R.id.noPlansText)
        updateNoPlansVisibility()

        // Configurar FloatingActionButton
        val fab = findViewById<FloatingActionButton>(R.id.fabAddPlan)
        fab.setOnClickListener {
            startCreatePlan(it)
        }
    }

    private fun handleNewPlan() {
        val intent = intent
        if (intent.hasExtra("new_plan")) {
            val planJson = intent.getStringExtra("new_plan")
            planJson?.let { json ->
                val plan = Gson().fromJson(json, StudyPlan::class.java)
                savePlan(plan)
                updateUI()
            }
        }
    }

    private fun startCreatePlan(view: View) {
        val intent = Intent(this, StudyPlanCreateStep1Activity::class.java)
        startActivityForResult(intent, CREATE_PLAN_REQUEST)
    }

    private fun updateUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = recyclerView.adapter as StudyPlanAdapter
        adapter.updatePlans(getPlans())
        updateNoPlansVisibility()
    }

    private fun updateNoPlansVisibility() {
        val noPlansText = findViewById<TextView>(R.id.noPlansText)
        noPlansText.visibility = if (getPlans().isEmpty()) View.VISIBLE else View.GONE
    }

    private fun getPlans(): List<StudyPlan> {
        return StudyPlanManager.getPlans(this)
    }

    private fun savePlan(plan: StudyPlan) {
        StudyPlanManager.savePlan(this, plan)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CREATE_PLAN_REQUEST) {
            data?.getStringExtra("plan")?.let { planJson ->
                val updatedPlan = Gson().fromJson(planJson, StudyPlan::class.java)
                savePlan(updatedPlan)
                updateUI()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}