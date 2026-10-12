package com.example.finanzasms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditarPerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val botonVolver = findViewById<TextView>(R.id.botonVolver)
        val nombre = findViewById<EditText>(R.id.editarNombre)
        val correo = findViewById<EditText>(R.id.editarCorreo)
        val estudios = findViewById<EditText>(R.id.editarEstudios)
        val experiencia = findViewById<EditText>(R.id.editarExperiencia)
        val guardar = findViewById<Button>(R.id.botonGuardar)

        val preferencias = getSharedPreferences(
            "perfil_usuario",
            MODE_PRIVATE
        )

        nombre.setText(preferencias.getString("nombre", ""))
        correo.setText(preferencias.getString("correo", ""))
        estudios.setText(preferencias.getString("estudios", ""))
        experiencia.setText(preferencias.getString("experiencia", ""))

        botonVolver.setOnClickListener {
            finish()
        }

        guardar.setOnClickListener {
            if (nombre.text.toString().isBlank()) {
                nombre.error = "Introduce tu nombre"
                return@setOnClickListener
            }

            preferencias.edit()
                .putString("nombre", nombre.text.toString().trim())
                .putString("correo", correo.text.toString().trim())
                .putString("estudios", estudios.text.toString().trim())
                .putString("experiencia", experiencia.text.toString().trim())
                .apply()

            Toast.makeText(
                this,
                "Perfil actualizado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}
