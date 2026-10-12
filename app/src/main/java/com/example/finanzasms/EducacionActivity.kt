
package com.example.finanzasms

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EducacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_educacion)


        // MENÚ HAMBURGUESA DE EDUCACIÓN FINANCIERA
        val botonMenuEducacion = findViewById<TextView>(
            R.id.botonMenuEducacion
        )

        botonMenuEducacion.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenuEducacion)
        }

        // Botón volver
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverEducacion
        )

        botonVolver.setOnClickListener {
            finish()
        }

        // Abrir primera lección
        val temaPresupuesto = findViewById<TextView>(
            R.id.temaPresupuesto
        )

        temaPresupuesto.setOnClickListener {
            val intent = Intent(this, LeccionActivity::class.java)
            intent.putExtra("numeroLeccion", 1)
            startActivity(intent)
        }

        val temaAhorro = findViewById<TextView>(
            R.id.temaAhorro
        )

        temaAhorro.setOnClickListener {
            val intent = Intent(this, LeccionActivity::class.java)
            intent.putExtra("numeroLeccion", 2)
            startActivity(intent)
        }

        // Abrir tercera lección: Créditos y deudas
        val temaDeudas = findViewById<TextView>(
            R.id.temaDeudas
        )

        temaDeudas.setOnClickListener {
            val intent = Intent(this, LeccionActivity::class.java)
            intent.putExtra("numeroLeccion", 3)
            startActivity(intent)
        }

        // Abrir cuarta lección: Inversiones
        val temaInversiones = findViewById<TextView>(
            R.id.temaInversiones
        )

        temaInversiones.setOnClickListener {
            val intent = Intent(this, LeccionActivity::class.java)
            intent.putExtra("numeroLeccion", 4)
            startActivity(intent)
        }

        }
    }

