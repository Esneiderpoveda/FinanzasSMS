package com.example.finanzasms

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

class CalculadoraPrestamosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calculadora_prestamos)

        // Conectar los elementos del XML
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverPrestamos
        )

        val inputMonto = findViewById<EditText>(
            R.id.inputMontoPrestamo
        )

        val inputInteres = findViewById<EditText>(
            R.id.inputInteresPrestamo
        )

        val inputMeses = findViewById<EditText>(
            R.id.inputMesesPrestamo
        )

        val botonCalcular = findViewById<Button>(
            R.id.botonCalcularPrestamo
        )

        val resultado = findViewById<TextView>(
            R.id.textResultadoPrestamo
        )

        // Regresar a Herramientas
        botonVolver.setOnClickListener {
            finish()
        }

        // Calcular préstamo
        botonCalcular.setOnClickListener {

            val monto = inputMonto.text.toString()
                .toDoubleOrNull()

            val interesAnual = inputInteres.text.toString()
                .replace(",", ".")
                .toDoubleOrNull()

            val meses = inputMeses.text.toString()
                .toIntOrNull()

            // Validar el monto
            if (monto == null || !monto.isFinite() || monto <= 0) {
                inputMonto.error = "Introduce un monto válido"
                return@setOnClickListener
            }

            // Validar el interés
            if (interesAnual == null ||
                !interesAnual.isFinite() ||
                interesAnual < 0
            ) {
                inputInteres.error = "Introduce un interés válido"
                return@setOnClickListener
            }

            // Validar los meses
            if (meses == null || meses <= 0) {
                inputMeses.error = "Introduce un plazo válido"
                return@setOnClickListener
            }

            // Convertir tasa nominal anual a tasa mensual
            val tasaMensual = interesAnual / 100.0 / 12.0

            // Calcular cuota mensual fija
            val cuotaMensual: Double

            if (tasaMensual == 0.0) {
                // Préstamo sin intereses
                cuotaMensual = monto / meses
            } else {
                val factor = (1 + tasaMensual).pow(meses)

                cuotaMensual = monto *
                        (tasaMensual * factor) / (factor - 1)
            }

            // Total pagado e intereses
            val totalPagar = cuotaMensual * meses
            val totalIntereses = totalPagar - monto

            if (!cuotaMensual.isFinite() ||
                !totalPagar.isFinite() ||
                !totalIntereses.isFinite()
            ) {
                resultado.text = "No se pudo calcular. Revisa los valores."
                return@setOnClickListener
            }

            // Formato de pesos colombianos
            val formato = NumberFormat.getNumberInstance(
                Locale("es", "CO")
            )

            formato.minimumFractionDigits = 0
            formato.maximumFractionDigits = 0

            val cuotaFormateada = formato.format(cuotaMensual)
            val totalFormateado = formato.format(totalPagar)
            val interesesFormateados = formato.format(totalIntereses)

            // Mostrar los resultados
            resultado.text = """
                RESULTADO DEL PRÉSTAMO

                Cuota mensual aproximada:
                $${cuotaFormateada} COP

                Total a pagar:
                $${totalFormateado} COP

                Total de intereses:
                $${interesesFormateados} COP

                Plazo: $meses meses
            """.trimIndent()
        }
    }
}
