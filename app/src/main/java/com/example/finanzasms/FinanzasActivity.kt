
package com.example.finanzasms

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class FinanzasActivity : AppCompatActivity() {

    private lateinit var spinnerPeriodo: Spinner
    private lateinit var spinnerMes: Spinner
    private lateinit var spinnerAnio: Spinner
    private lateinit var contenedorMesAnio: LinearLayout

    private lateinit var textIngresos: TextView
    private lateinit var textEgresos: TextView

    private val formatoMoneda = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-CO")
    ).apply {
        maximumFractionDigits = 0
    }

    private val meses = listOf(
        "Enero", "Febrero", "Marzo", "Abril",
        "Mayo", "Junio", "Julio", "Agosto",
        "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )

    private var anios = listOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_finanzas)

        // MENÚ HAMBURGUESA
        val botonMenuFinanzas = findViewById<TextView>(
            R.id.botonMenuFinanzas
        )

        botonMenuFinanzas.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenuFinanzas)
        }


        // BOTÓN VOLVER
        val botonVolver = findViewById<TextView>(
            R.id.botonVolverFinanzas
        )

        botonVolver.setOnClickListener {
            finish()
        }

        // REFERENCIAS DE LOS FILTROS
        spinnerPeriodo = findViewById(R.id.spinnerPeriodo)
        spinnerMes = findViewById(R.id.spinnerMes)
        spinnerAnio = findViewById(R.id.spinnerAnio)
        contenedorMesAnio = findViewById(R.id.contenedorMesAnio)

        // REFERENCIAS DE LOS TOTALES
        textIngresos = findViewById(R.id.textTotalIngresos)
        textEgresos = findViewById(R.id.textTotalEgresos)

        configurarFiltros()

        // Mostrar inicialmente todo el historial
        actualizarFinanzas()
    }

    // ----------------------------------------
    // CONFIGURAR LOS FILTROS
    // ----------------------------------------

    private fun configurarFiltros() {

        val periodos = listOf(
            "Todo el historial",
            "Este mes",
            "Últimos 7 días",
            "Elegir mes y año"
        )

        val adaptadorPeriodos = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            periodos
        )

        adaptadorPeriodos.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerPeriodo.adapter = adaptadorPeriodos

        // CONFIGURAR MESES
        val adaptadorMeses = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            meses
        )

        adaptadorMeses.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerMes.adapter = adaptadorMeses

        // CONFIGURAR AÑOS
        val calendario = Calendar.getInstance()
        val anioActual = calendario.get(Calendar.YEAR)

        anios = (2024..anioActual).toList().reversed()

        val adaptadorAnios = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            anios.map { it.toString() }
        )

        adaptadorAnios.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerAnio.adapter = adaptadorAnios

        // SELECCIONAR MES Y AÑO ACTUALES
        spinnerMes.setSelection(
            calendario.get(Calendar.MONTH)
        )

        spinnerAnio.setSelection(
            anios.indexOf(anioActual)
        )

        // ESCUCHAR CAMBIOS DEL PERÍODO
        spinnerPeriodo.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    contenedorMesAnio.visibility =
                        if (position == 3) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                    actualizarFinanzas()
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        // ESCUCHAR CAMBIOS DEL MES
        spinnerMes.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (spinnerPeriodo.selectedItemPosition == 3) {
                        actualizarFinanzas()
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }

        // ESCUCHAR CAMBIOS DEL AÑO
        spinnerAnio.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (spinnerPeriodo.selectedItemPosition == 3) {
                        actualizarFinanzas()
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                }
            }
    }

    // ----------------------------------------
    // ACTUALIZAR LAS FINANZAS
    // ----------------------------------------

    private fun actualizarFinanzas() {

        // Recuperar las transacciones guardadas
        val transacciones = Almacenamiento.obtenerHistorial(this)

        val calendarioActual = Calendar.getInstance()

        val ahora = calendarioActual.timeInMillis

        // Filtrar según la opción seleccionada
        val transaccionesFiltradas = when (
            spinnerPeriodo.selectedItemPosition
        ) {

            // ESTE MES
            1 -> {

                val inicioMes = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                transacciones.filter {
                    it.fecha in inicioMes..ahora
                }
            }

            // ÚLTIMOS 7 DÍAS
            2 -> {

                val inicio = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }.timeInMillis

                transacciones.filter {
                    it.fecha in inicio..ahora
                }
            }

            // ELEGIR MES Y AÑO
            3 -> {

                val mesSeleccionado = spinnerMes.selectedItemPosition

                val anioSeleccionado = anios.getOrNull(
                    spinnerAnio.selectedItemPosition
                ) ?: calendarioActual.get(Calendar.YEAR)

                val inicio = Calendar.getInstance().apply {
                    clear()
                    set(
                        anioSeleccionado,
                        mesSeleccionado,
                        1,
                        0,
                        0,
                        0
                    )
                }

                val fin = (inicio.clone() as Calendar).apply {
                    add(Calendar.MONTH, 1)
                }

                transacciones.filter {
                    it.fecha >= inicio.timeInMillis &&
                            it.fecha < fin.timeInMillis
                }
            }

            // TODO EL HISTORIAL
            else -> transacciones
        }

        // CALCULAR INGRESOS
        val totalIngresos = transaccionesFiltradas
            .filter { it.tipo == "INGRESO" }
            .sumOf { it.monto }

        // CALCULAR EGRESOS
        val totalEgresos = transaccionesFiltradas
            .filter { it.tipo == "EGRESO" }
            .sumOf { it.monto }

        // MOSTRAR TOTALES
        textIngresos.text = formatoMoneda.format(totalIngresos)
        textEgresos.text = formatoMoneda.format(totalEgresos)

        // AGRUPAR MOVIMIENTOS POR ENTIDAD
        val movimientosPorEntidad = transaccionesFiltradas.groupBy {
            IdentificadorEntidad.identificar(it)
        }

        // SUMAR DINERO MOVIDO POR ENTIDAD
        val totalesPorEntidad = movimientosPorEntidad.mapValues {
                (_, movimientos) ->
            movimientos.sumOf { it.monto }
        }

        // ORDENAR DE MAYOR A MENOR
        val entidadesOrdenadas = totalesPorEntidad
            .toList()
            .sortedByDescending { it.second }

        // ACTUALIZAR EL GRÁFICO
        mostrarGrafico(entidadesOrdenadas)
    }

    // ----------------------------------------
    // MOSTRAR GRÁFICO POR ENTIDAD FINANCIERA
    // ----------------------------------------

    private fun mostrarGrafico(
        entidades: List<Pair<String, Long>>
    ) {

        val contenedor = findViewById<LinearLayout>(
            R.id.contenedorGrafico
        )

        // Limpiar las barras anteriores
        contenedor.removeAllViews()

        // Mensaje cuando no existan movimientos
        if (entidades.isEmpty()) {

            val mensaje = TextView(this).apply {
                text = "No hay movimientos en este período"
                textSize = 16f
                setTextColor(Color.GRAY)
            }

            contenedor.addView(mensaje)
            return
        }

        // Buscar el importe más alto
        val montoMaximo = entidades.maxOf { it.second }

        // Crear una barra por entidad
        entidades.forEach { (nombre, monto) ->

            // Nombre e importe
            val titulo = TextView(this).apply {
                text = "$nombre: ${formatoMoneda.format(monto)}"
                textSize = 16f
                setTextColor(Color.DKGRAY)
                setPadding(0, 20, 0, 8)
            }

            contenedor.addView(titulo)

            // Fondo gris
            val fondoBarra = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setBackgroundColor(Color.LTGRAY)

                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    28
                )
            }

            // Proporción de la barra
            val porcentaje = if (montoMaximo > 0L) {
                monto.toDouble() / montoMaximo.toDouble()
            } else {
                0.0
            }

            // Barra azul
            val barra = View(this).apply {
                setBackgroundColor(
                    Color.rgb(33, 150, 243)
                )
            }

            fondoBarra.addView(
                barra,
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    porcentaje.toFloat()
                )
            )

            // Espacio restante
            val espacioRestante = View(this)

            fondoBarra.addView(
                espacioRestante,
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    (1.0 - porcentaje).toFloat()
                )
            )

            contenedor.addView(fondoBarra)
        }
    }
}
