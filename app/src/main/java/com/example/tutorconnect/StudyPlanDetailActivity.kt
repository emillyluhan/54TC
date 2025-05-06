package com.example.tutorconnect

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.annotation.IdRes
import com.example.tutorconnect.model.StudyPlan
import com.example.tutorconnect.utils.StudyPlanManager
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class StudyPlanDetailActivity : AppCompatActivity() {
    private var plan: StudyPlan? = null
    private var dialog: AlertDialog? = null
    private var progressDialog: AlertDialog? = null

    private inline fun <reified T : View> findViewByIdCompat(@IdRes id: Int): T {
        return findViewById(id)
    }

    companion object {
        private const val DETAIL_PLAN_REQUEST = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val planJson = intent.getStringExtra("plan")
        plan = Gson().fromJson(planJson, StudyPlan::class.java)
        
        setContentView(R.layout.activity_study_plan_detail)
        setupUI()
    }

    private fun setupUI() {
        val plan = plan ?: return
        
        setupToolbar()

        // Configurar los campos
        findViewById<TextView>(R.id.subjectText).text = plan.subject
        findViewById<TextView>(R.id.datesText).text = "${plan.startDate} - ${plan.endDate}"
        findViewById<TextView>(R.id.objectiveText).text = plan.objective
        findViewById<TextView>(R.id.topicsText).text = plan.topics.joinToString("\n")
        findViewById<TextView>(R.id.timeText).text = "${plan.timePerWeek} horas/semana"
        findViewById<TextView>(R.id.progressDetail).text = plan.getProgressPercentage()
        findViewById<TextView>(R.id.lastUpdatedDetail).text = formatLastUpdated(plan.lastUpdated)

        // Configurar botones
        findViewById<FloatingActionButton>(R.id.fabEdit).apply {
            visibility = View.VISIBLE
            setOnClickListener {
                val intent = Intent(this@StudyPlanDetailActivity, StudyPlanCreateStep1Activity::class.java)
                intent.putExtra("plan", Gson().toJson(plan))
                startActivityForResult(intent, DETAIL_PLAN_REQUEST)
            }
        }

        findViewById<Button>(R.id.btnComplete).apply {
            visibility = View.VISIBLE
            setOnClickListener {
                plan?.let { updatedPlan ->
                    updatedPlan.markAsCompleted()
                    StudyPlanManager.savePlan(this@StudyPlanDetailActivity, updatedPlan)
                    finish()
                }
            }
        }

        findViewById<Button>(R.id.btnUpdateProgress).setOnClickListener {
            showUpdateProgressDialog()
        }

        findViewById<FloatingActionButton>(R.id.fabDelete).setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Plan")
            .setMessage("¿Estás seguro de que quieres eliminar este plan?")
            .setPositiveButton("Eliminar") { _, _ ->
                plan?.let { deletedPlan ->
                    StudyPlanManager.deletePlan(this, deletedPlan)
                    finish()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun formatLastUpdated(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            title = plan?.subject
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showUpdateProgressDialog() {
        val plan = plan ?: return
        
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_update_progress, null)

        val seekBar = dialogView.findViewById<AppCompatSeekBar>(R.id.progressSeekBar)
        val currentProgressText = dialogView.findViewById<AppCompatTextView>(R.id.currentProgressText)
        val selectedProgressText = dialogView.findViewById<AppCompatTextView>(R.id.selectedProgressText)
        val completeButton = dialogView.findViewById<AppCompatButton>(R.id.btnComplete)

        // Configurar el SeekBar
        seekBar.progress = (plan.progress * 100).toInt()
        currentProgressText.text = "Progreso actual: ${plan.getProgressPercentage()}"
        selectedProgressText.text = "Progreso seleccionado: ${plan.getProgressPercentage()}"

        // Deshabilitar SeekBar si el plan está completado
        seekBar.isEnabled = plan?.canUpdateProgress() ?: false

        // Configurar botones
        completeButton.setOnClickListener {
            plan?.let { updatedPlan ->
                updatedPlan.markAsCompleted()
                StudyPlanManager.savePlan(this@StudyPlanDetailActivity, updatedPlan)
                finish()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                selectedProgressText.text = "Progreso seleccionado: $progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configurar botones
        completeButton.setOnClickListener {
            plan?.let { updatedPlan ->
                updatedPlan.markAsCompleted()
                StudyPlanManager.savePlan(this, updatedPlan)
                finish()
            }
        }

        val builder = AlertDialog.Builder(this@StudyPlanDetailActivity)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                plan?.let { updatedPlan ->
                    updatedPlan.updateProgress(seekBar.progress / 100f)
                    StudyPlanManager.savePlan(this@StudyPlanDetailActivity, updatedPlan)
                    setupUI()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        progressDialog = builder
        progressDialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DETAIL_PLAN_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("plan")?.let { planJson ->
                val updatedPlan = Gson().fromJson(planJson, StudyPlan::class.java)
                StudyPlanManager.savePlan(this, updatedPlan)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
