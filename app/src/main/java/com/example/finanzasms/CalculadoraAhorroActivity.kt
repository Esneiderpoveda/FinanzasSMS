package com.example.finanzasms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class CalculadoraAhorroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calculadora_ahorro)

        // Conectar elementos del XML
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverAhorro
        )

        val inputAhorro = findViewById<EditText>(
            R.id.inputAhorroMensual
        )

        val inputMeses = findViewById<EditText>(
            R.id.inputMesesAhorro
        )

        val botonCalcular = findViewById<Button>(
            R.id.botonCalcularAhorro
        )

        val resultado = findViewById<TextView>(
            R.id.textResultadoAhorro
        )

        // Regresar a Herramientas
        botonVolver.setOnClickListener {
            finish()
        }

        // Calcular ahorro
        botonCalcular.setOnClickListener {

            val ahorroMensual = inputAhorro.text.toString()
                .toLongOrNull()

            val meses = inputMeses.text.toString()
                .toLongOrNull()

            // Validar los datos
            if (ahorroMensual == null || ahorroMensual <= 0) {
                inputAhorro.error = "Introduce una cantidad válida"
                return@setOnClickListener
            }

            if (meses == null || meses <= 0) {
                inputMeses.error = "Introduce un número de meses válido"
                return@setOnClickListener
            }

            // Multiplicar ahorro mensual por meses
            val totalAhorro = ahorroMensual.toDouble() * meses

            // Dar formato al resultado en pesos colombianos
            val formato = NumberFormat.getNumberInstance(
                Locale("es", "CO")
            )

            formato.maximumFractionDigits = 0

            val totalFormateado = formato.format(totalAhorro)

            // Mostrar resultado
            resultado.text = """
                ¡Este será tu ahorro acumulado!

                $$totalFormateado COP

                Ahorrando durante $meses meses.
            """.trimIndent()
        }
    }
}
