package com.example.finanzasms

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Menú hamburguesa de Perfil
        val botonMenuPerfil = findViewById<TextView>(
            R.id.botonMenuPerfil
        )

        botonMenuPerfil.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenuPerfil)
        }


        // Volver a Inicio
        findViewById<TextView>(R.id.botonVolver)
            .setOnClickListener {
                finish()
            }

        // Abrir Editar perfil
        findViewById<Button>(R.id.botonEditarPerfil)
            .setOnClickListener {
                val intent = Intent(
                    this,
                    EditarPerfilActivity::class.java
                )
                startActivity(intent)
            }
    }

    override fun onResume() {
        super.onResume()
        cargarPerfil()
    }

    private fun cargarPerfil() {
        val preferencias = getSharedPreferences(
            "perfil_usuario",
            MODE_PRIVATE
        )

        val nombre = preferencias.getString("nombre", "")
        val correo = preferencias.getString("correo", "")
        val estudios = preferencias.getString("estudios", "")
        val experiencia = preferencias.getString("experiencia", "")

        findViewById<TextView>(R.id.textNombre).text =
            nombre?.ifBlank { "Tu nombre" }

        findViewById<TextView>(R.id.textProfesion).text =
            estudios?.ifBlank { "Tu profesión" }

        findViewById<TextView>(R.id.textCorreo).text =
            "Correo\n${correo.orEmpty()}"

        findViewById<TextView>(R.id.textEstudios).text =
            "Estudios\n${estudios.orEmpty()}"

        findViewById<TextView>(R.id.textExperiencia).text =
            "Experiencia\n${experiencia.orEmpty()}"
    }
}
