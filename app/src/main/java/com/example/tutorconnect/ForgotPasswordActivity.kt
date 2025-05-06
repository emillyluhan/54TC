package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Referencia al botón personalizado (LinearLayout)
        val btnRecuperar = findViewById<LinearLayout>(R.id.btnRecuperar)

        // Referencia al campo de email
        val emailInput = findViewById<EditText>(R.id.emailInput)

        // Evento al hacer clic en "Recuperar"
        btnRecuperar.setOnClickListener {
            val email = emailInput.text.toString().trim()

            // Aquí podrías validar el correo si deseas

            // Ir a pantalla de confirmación
            val intent = Intent(this, ConfirmacionRecuperacionActivity::class.java)
            startActivity(intent)
        }
    }
}
