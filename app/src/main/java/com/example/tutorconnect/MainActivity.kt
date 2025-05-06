package com.example.tutorconnect

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ir a pantalla de registro
        val btnRegister = findViewById<TextView>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Ir a pantalla de recuperación de contraseña
        val txtForgotPassword = findViewById<TextView>(R.id.txtForgotPassword)
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Ir a HomeActivity o StudentHomeActivity según el checkbox
        val btnLogin = findViewById<LinearLayout>(R.id.btnLogin)
        val teacherCheckbox = findViewById<CheckBox>(R.id.teacherCheckbox)
        
        btnLogin.setOnClickListener {
            val intent = if (teacherCheckbox.isChecked) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, StudentHomeActivity::class.java)
            }
            startActivity(intent)
        }
    }
}
