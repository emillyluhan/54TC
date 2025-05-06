package com.example.tutorconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class TutorDatosFragment : Fragment() {
    private var isEditing = false
    private lateinit var viewMode: View
    private lateinit var editMode: View
    private lateinit var btnEditar: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutor_datos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener referencias a las vistas
        viewMode = view.findViewById(R.id.viewMode)
        editMode = view.findViewById(R.id.editMode)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnCancelar = view.findViewById(R.id.btnCancelar)

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

    private fun startEditing() {
        isEditing = true
        viewMode.visibility = View.GONE
        editMode.visibility = View.VISIBLE
        
        // Copiar datos de TextViews a EditTexts
        view?.apply {
            findViewById<EditText>(R.id.etNombre)?.setText(findViewById<TextView>(R.id.tvNombre)?.text)
            findViewById<EditText>(R.id.etEdad)?.setText(findViewById<TextView>(R.id.tvEdad)?.text)
            findViewById<EditText>(R.id.etEspecialidad)?.setText(findViewById<TextView>(R.id.tvEspecialidad)?.text)
            findViewById<EditText>(R.id.etHorario)?.setText(findViewById<TextView>(R.id.tvHorario)?.text)
            findViewById<EditText>(R.id.etModalidades)?.setText(findViewById<TextView>(R.id.tvModalidades)?.text)
            findViewById<EditText>(R.id.etEmail)?.setText(findViewById<TextView>(R.id.tvEmail)?.text)
            findViewById<EditText>(R.id.etTelefono)?.setText(findViewById<TextView>(R.id.tvTelefono)?.text)
            findViewById<EditText>(R.id.etWhatsapp)?.setText(findViewById<TextView>(R.id.tvWhatsapp)?.text)
            findViewById<EditText>(R.id.etUbicacion)?.setText(findViewById<TextView>(R.id.tvUbicacion)?.text)
        }
    }

    private fun saveChanges() {
        isEditing = false
        viewMode.visibility = View.VISIBLE
        editMode.visibility = View.GONE
        
        // Guardar cambios y actualizar TextViews
        view?.apply {
            findViewById<TextView>(R.id.tvNombre)?.text = findViewById<EditText>(R.id.etNombre)?.text
            findViewById<TextView>(R.id.tvEdad)?.text = findViewById<EditText>(R.id.etEdad)?.text
            findViewById<TextView>(R.id.tvEspecialidad)?.text = findViewById<EditText>(R.id.etEspecialidad)?.text
            findViewById<TextView>(R.id.tvHorario)?.text = findViewById<EditText>(R.id.etHorario)?.text
            findViewById<TextView>(R.id.tvModalidades)?.text = findViewById<EditText>(R.id.etModalidades)?.text
            findViewById<TextView>(R.id.tvEmail)?.text = findViewById<EditText>(R.id.etEmail)?.text
            findViewById<TextView>(R.id.tvTelefono)?.text = findViewById<EditText>(R.id.etTelefono)?.text
            findViewById<TextView>(R.id.tvWhatsapp)?.text = findViewById<EditText>(R.id.etWhatsapp)?.text
            findViewById<TextView>(R.id.tvUbicacion)?.text = findViewById<EditText>(R.id.etUbicacion)?.text
        }

        // Aquí podrías guardar los cambios en tu base de datos o API
        // TODO: Implementar la lógica de guardado
    }

    private fun cancelEditing() {
        isEditing = false
        viewMode.visibility = View.VISIBLE
        editMode.visibility = View.GONE
    }

    private fun loadData() {
        // Cargar datos iniciales en modo visualización
        view?.apply {
            findViewById<TextView>(R.id.tvNombre)?.text = "María González Tatiana"
            findViewById<TextView>(R.id.tvEdad)?.text = "28 años"
            findViewById<TextView>(R.id.tvEspecialidad)?.text = "Matemáticas y Física"
            findViewById<TextView>(R.id.tvHorario)?.text = "Lunes a viernes de 6:00 p.m. a 10:00 p.m."
            findViewById<TextView>(R.id.tvModalidades)?.text = "Modalidades: Presencial y virtual"
            findViewById<TextView>(R.id.tvEmail)?.text = "maria.gonzalez@tutorconnect.com"
            findViewById<TextView>(R.id.tvTelefono)?.text = "+57 312 345 6789"
            findViewById<TextView>(R.id.tvWhatsapp)?.text = "+57 312 345 6789"
            findViewById<TextView>(R.id.tvUbicacion)?.text = "Bogotá, Colombia"
        }
    }
} 