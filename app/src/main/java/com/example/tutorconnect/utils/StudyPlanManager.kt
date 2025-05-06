package com.example.tutorconnect.utils

import android.content.Context
import com.example.tutorconnect.model.StudyPlan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StudyPlanManager {
    private const val PREFS_NAME = "StudyPlanPrefs"
    private const val KEY_PLANS = "study_plans"

    fun getPlans(context: Context): List<StudyPlan> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_PLANS, null)
        
        return if (json != null) {
            val type = object : TypeToken<List<StudyPlan>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun savePlan(context: Context, plan: StudyPlan) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val plans = getPlans(context).toMutableList()
        
        // Buscar y reemplazar el plan existente
        val index = plans.indexOfFirst { it.id == plan.id }
        if (index != -1) {
            plans[index] = plan
        } else {
            plans.add(plan)
        }
        
        editor.putString(KEY_PLANS, Gson().toJson(plans))
        editor.apply()
    }

    fun deletePlan(context: Context, planToDelete: StudyPlan) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val plans = getPlans(context).toMutableList()
        plans.removeIf { it == planToDelete }
        editor.putString(KEY_PLANS, Gson().toJson(plans))
        editor.apply()
    }
}
