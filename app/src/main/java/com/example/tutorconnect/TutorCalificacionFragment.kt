package com.example.tutorconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TutorCalificacionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_calificacion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar la calificación general
        view.findViewById<TextView>(R.id.tvCalificacionGeneral)?.text = "5,0/5,0"
        view.findViewById<RatingBar>(R.id.ratingBarGeneral)?.rating = 5.0f

        // Configurar el RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCalificaciones)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = CalificacionesAdapter(getCalificacionesEjemplo())

        // Configurar el estilo del RatingBar
        view.findViewById<RatingBar>(R.id.ratingBarGeneral)?.apply {
            progressTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#6A1B9A"))
            secondaryProgressTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#6A1B9A"))
        }
    }

    private fun getCalificacionesEjemplo(): List<Calificacion> {
        return listOf(
            Calificacion("Juan Pérez", 5.0f),
            Calificacion("Ana García", 4.0f),
            Calificacion("Carlos López", 5.0f)
        )
    }
}

data class Calificacion(
    val nombreEstudiante: String,
    val calificacion: Float
)

class CalificacionesAdapter(private val calificaciones: List<Calificacion>) :
    RecyclerView.Adapter<CalificacionesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreEstudiante: TextView = view.findViewById(R.id.tvNombreEstudiante)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBarEstudiante)
        val tvCalificacion: TextView = view.findViewById(R.id.tvCalificacion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calificacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calificacion = calificaciones[position]
        holder.tvNombreEstudiante.text = calificacion.nombreEstudiante
        holder.ratingBar.rating = calificacion.calificacion
        holder.tvCalificacion.text = String.format("%.1f/5.0", calificacion.calificacion)
    }

    override fun getItemCount() = calificaciones.size
} 