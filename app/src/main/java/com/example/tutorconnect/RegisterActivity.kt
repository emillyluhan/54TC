package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Referencias a los views
        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val switchTutor = findViewById<Switch>(R.id.switchTutor)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // Animación opcional para el botón
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)

        btnRegister.setOnClickListener {
            btnRegister.startAnimation(bounceAnim) // animación rebote

            val nombre = etName.text.toString().trim()
            val correo = etEmail.text.toString().trim()
            val telefono = etPhone.text.toString().trim()
            val contraseña = etPassword.text.toString()
            val esTutor = switchTutor.isChecked

            // Validaciones básicas
            when {
                nombre.isEmpty() -> {
                    etName.error = "Nombre requerido"
                    etName.requestFocus()
                    return@setOnClickListener
                }
                correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                    etEmail.error = "Correo inválido"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }
                telefono.isEmpty() || telefono.length < 8 -> {
                    etPhone.error = "Teléfono inválido"
                    etPhone.requestFocus()
                    return@setOnClickListener
                }
                contraseña.isEmpty() || contraseña.length < 6 -> {
                    etPassword.error = "La contraseña debe tener al menos 6 caracteres"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                else -> {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                    // Ir a la pantalla de inicio de sesión
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}


