package com.example.tutorconnect

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class StudentDatosFragment : Fragment() {
    private var isEditing = false
    private lateinit var viewMode: View
    private lateinit var editMode: View
    private lateinit var btnEditar: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_datos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a las vistas
        viewMode = view.findViewById(R.id.viewMode)
        editMode = view.findViewById(R.id.editMode)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
        prefs = requireActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE)

        // Configurar los botones
        btnEditar.setOnClickListener {
            startEditing()
        }

        btnGuardar.setOnClickListener {
            saveChanges()
        }

        btnCancelar.setOnClickListener {
            cancelEditing()
        }

        // Cargar datos iniciales
        loadData()
    }

    private fun loadData() {
        view?.apply {
            findViewById<TextView>(R.id.tvName)?.text = prefs.getString("student_name", "Nombre del estudiante")
            findViewById<TextView>(R.id.tvEmail)?.text = prefs.getString("student_email", "email@ejemplo.com")
            findViewById<TextView>(R.id.tvPhone)?.text = prefs.getString("student_phone", "+57 300 123 4567")
            findViewById<TextView>(R.id.tvAge)?.text = "Edad: ${prefs.getInt("student_age", 22)} años"
            findViewById<TextView>(R.id.tvProgram)?.text = prefs.getString("student_program", "Ingeniería de Sistemas")
            findViewById<TextView>(R.id.tvFavoriteSubjects)?.text = prefs.getString("student_favorites", "Álgebra Lineal, Diferencial")
        }
    }

    private fun startEditing() {
        isEditing = true
        viewMode.visibility = View.GONE
        editMode.visibility = View.VISIBLE
        
        // Copiar datos de TextViews a EditTexts
        view?.apply {
            findViewById<EditText>(R.id.etName)?.setText(prefs.getString("student_name", "Nombre del estudiante"))
            findViewById<EditText>(R.id.etEmail)?.setText(prefs.getString("student_email", "email@ejemplo.com"))
            findViewById<EditText>(R.id.etPhone)?.setText(prefs.getString("student_phone", "+57 300 123 4567"))
            findViewById<EditText>(R.id.etAge)?.setText(prefs.getInt("student_age", 22).toString())
            findViewById<EditText>(R.id.etProgram)?.setText(prefs.getString("student_program", "Ingeniería de Sistemas"))
            findViewById<EditText>(R.id.etFavoriteSubjects)?.setText(prefs.getString("student_favorites", "Álgebra Lineal, Diferencial"))
        }
    }

    private fun saveChanges() {
        isEditing = false
        viewMode.visibility = View.VISIBLE
        editMode.visibility = View.GONE
        
        // Guardar cambios en SharedPreferences
        val editor = prefs.edit()
        view?.apply {
            editor.putString("student_name", findViewById<EditText>(R.id.etName)?.text.toString())
            editor.putString("student_email", findViewById<EditText>(R.id.etEmail)?.text.toString())
            editor.putString("student_phone", findViewById<EditText>(R.id.etPhone)?.text.toString())
            editor.putInt("student_age", findViewById<EditText>(R.id.etAge)?.text.toString().toIntOrNull() ?: 22)
            editor.putString("student_program", findViewById<EditText>(R.id.etProgram)?.text.toString())
            editor.putString("student_favorites", findViewById<EditText>(R.id.etFavoriteSubjects)?.text.toString())
            editor.apply()
        }

        // Actualizar TextViews
        loadData()
    }

    private fun cancelEditing() {
        isEditing = false
        viewMode.visibility = View.VISIBLE
        editMode.visibility = View.GONE
    }
}
