package com.example.finanzasms

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class HerramientasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_herramientas)


        // MENÚ HAMBURGUESA DE HERRAMIENTAS
        val botonMenuHerramientas = findViewById<TextView>(
            R.id.botonMenuHerramientas
        )

        botonMenuHerramientas.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenuHerramientas)
        }


        // Botón para regresar al inicio
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverHerramientas
        )

        botonVolver.setOnClickListener {
            finish()
        }

        // Calculadora de ahorro
        val herramientaAhorro = findViewById<TextView>(
            R.id.herramientaAhorro
        )

        herramientaAhorro.setOnClickListener {
            val intent = Intent(
                this,
                CalculadoraAhorroActivity::class.java
            )
            startActivity(intent)
        }

        // Calculadora de préstamos
        val herramientaPrestamos = findViewById<TextView>(
            R.id.herramientaPrestamos
        )


        herramientaPrestamos.setOnClickListener {
            val intent = Intent(
                this,
                CalculadoraPrestamosActivity::class.java
            )
            startActivity(intent)
        }

    }
}
