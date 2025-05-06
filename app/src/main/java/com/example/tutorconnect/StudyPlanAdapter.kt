package com.example.tutorconnect

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tutorconnect.model.StudyPlan
import com.example.tutorconnect.R
import com.example.tutorconnect.StudyPlanDetailActivity
import com.example.tutorconnect.StudyPlanCreateStep1Activity
import com.example.tutorconnect.utils.StudyPlanManager
import com.google.gson.Gson

class StudyPlanAdapter(
    private var plans: List<StudyPlan>,
    private val context: Context
) :
    RecyclerView.Adapter<StudyPlanAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subject: TextView = itemView.findViewById(R.id.subject)
        val dates: TextView = itemView.findViewById(R.id.dates)
        val time: TextView = itemView.findViewById(R.id.time)
        val progress: TextView = itemView.findViewById(R.id.progress)
        val status: TextView = itemView.findViewById(R.id.status)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_study_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = plans[position]
        holder.subject.text = plan.subject
        holder.dates.text = "${plan.startDate} - ${plan.endDate}"
        holder.time.text = "${plan.timePerWeek} horas/semana"
        holder.progress.text = plan.getProgressPercentage()
        holder.status.text = if (plan.isCompleted) "Finalizado" else "En curso"
        holder.status.setTextColor(holder.itemView.context.getColor(
            if (plan.isCompleted) android.R.color.holo_green_dark else R.color.purple_dark
        ))

        holder.itemView.setOnClickListener {
            val intent = Intent(context, StudyPlanDetailActivity::class.java)
            intent.putExtra("plan", Gson().toJson(plan))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            showPlanOptions(context, plan, holder)
            true
        }

        holder.itemView.setOnLongClickListener {
            showPlanOptions(context, plan, holder)
            true
        }
    }

    private fun showPlanOptions(context: Context, plan: StudyPlan, holder: ViewHolder) {
        val popup = PopupMenu(context, holder.itemView)
        popup.menuInflater.inflate(R.menu.plan_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(context, StudyPlanCreateStep1Activity::class.java)
                    intent.putExtra("plan", Gson().toJson(plan))
                    context.startActivity(intent)
                    true
                }
                R.id.action_delete -> {
                    showDeleteConfirmation(context, plan, holder)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showDeleteConfirmation(context: Context, plan: StudyPlan, holder: ViewHolder) {
        AlertDialog.Builder(context)
            .setTitle("Eliminar Plan")
            .setMessage("¿Estás seguro de que quieres eliminar este plan?")
            .setPositiveButton("Eliminar") { _, _ ->
                StudyPlanManager.deletePlan(context, plan)
                updatePlans(StudyPlanManager.getPlans(context))
                context.startActivity(Intent(context, StudyPlanListActivity::class.java))
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun getItemCount() = plans.size

    fun updatePlans(newPlans: List<StudyPlan>) {
        plans = newPlans
        notifyDataSetChanged()
    }
}