
package com.example.finanzasms

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var textSaldoTotal: TextView
    private lateinit var textEstadoPermisos: TextView
    private lateinit var botonSolicitarPermisos: Button
    private lateinit var botonResetear: Button
    private lateinit var botonAjustarSaldo: Button
    private lateinit var recyclerHistorial: RecyclerView
    private lateinit var adapter: TransaccionAdapter

    private val formatoMoneda: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("es", "CO"))

    private val permisosNecesarios = arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_SMS
    )

    // Solicitar permisos para leer y recibir SMS
    private val solicitudPermisos = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultados ->

        val todosConcedidos = resultados.values.all { it }

        if (todosConcedidos) {
            Toast.makeText(
                this,
                "Permisos concedidos correctamente",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "Sin los permisos de SMS la app no puede detectar tus movimientos",
                Toast.LENGTH_LONG
            ).show()
        }

        actualizarEstadoPermisos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // ----------------------------------------
        // BOTÓN ACTUALIZAR MOVIMIENTOS
        // ----------------------------------------

        val botonActualizar =
            findViewById<Button>(R.id.botonActualizar)

        botonActualizar.setOnClickListener {

            cargarDatos()

            Toast.makeText(
                this,
                "Movimientos actualizados",
                Toast.LENGTH_SHORT
            ).show()
        }

        // ----------------------------------------
        // MENÚ HAMBURGUESA
        // ----------------------------------------


        val botonMenu = findViewById<TextView>(R.id.botonMenu)

        botonMenu.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenu)
        }


        // ----------------------------------------
        // REFERENCIAS DE LA PANTALLA
        // ----------------------------------------

        textSaldoTotal =
            findViewById(R.id.textSaldoTotal)

        textEstadoPermisos =
            findViewById(R.id.textEstadoPermisos)

        botonSolicitarPermisos =
            findViewById(R.id.botonSolicitarPermisos)

        botonResetear =
            findViewById(R.id.botonResetear)

        botonAjustarSaldo =
            findViewById(R.id.botonAjustarSaldo)

        recyclerHistorial =
            findViewById(R.id.recyclerHistorial)

        // ----------------------------------------
        // CONFIGURAR HISTORIAL
        // ----------------------------------------

        adapter = TransaccionAdapter(emptyList())

        recyclerHistorial.layoutManager =
            LinearLayoutManager(this)

        recyclerHistorial.adapter = adapter

        // ----------------------------------------
        // BOTONES PRINCIPALES
        // ----------------------------------------

        botonSolicitarPermisos.setOnClickListener {
            solicitudPermisos.launch(permisosNecesarios)
        }

        botonResetear.setOnClickListener {
            mostrarConfirmacionReseteo()
        }

        botonAjustarSaldo.setOnClickListener {
            mostrarDialogoAjusteSaldo()
        }

        actualizarEstadoPermisos()
        cargarDatos()
    }

    override fun onResume() {
        super.onResume()

        // Actualizar datos al volver a la aplicación
        actualizarEstadoPermisos()
        cargarDatos()
    }

    // ----------------------------------------
    // CARGAR SALDO E HISTORIAL
    // ----------------------------------------

    private fun cargarDatos() {

        val saldo = Almacenamiento.obtenerSaldo(this)

        textSaldoTotal.text = formatoMoneda.format(saldo)

        val historial = Almacenamiento.obtenerHistorial(this)

        adapter.actualizarDatos(historial)
    }

    // ----------------------------------------
    // COMPROBAR PERMISOS
    // ----------------------------------------

    private fun tienePermisosConcedidos(): Boolean {

        return permisosNecesarios.all {
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun actualizarEstadoPermisos() {

        if (tienePermisosConcedidos()) {

            textEstadoPermisos.text =
                "Permisos de SMS concedidos"

            botonSolicitarPermisos.visibility = View.GONE

        } else {

            textEstadoPermisos.text =
                "Se necesitan permisos de SMS para detectar movimientos"

            botonSolicitarPermisos.visibility = View.VISIBLE
        }
    }

    // ----------------------------------------
    // RESETEAR SALDO E HISTORIAL
    // ----------------------------------------

    private fun mostrarConfirmacionReseteo() {

        AlertDialog.Builder(this)
            .setTitle("Resetear saldo")
            .setMessage(
                "¿Seguro que deseas borrar el saldo y todo el historial " +
                        "de movimientos? Esta acción no se puede deshacer."
            )
            .setPositiveButton("Sí, resetear") { _, _ ->

                Almacenamiento.resetear(this)

                cargarDatos()

                Toast.makeText(
                    this,
                    "Saldo e historial reseteados",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // ----------------------------------------
    // AJUSTAR SALDO MANUALMENTE
    // ----------------------------------------

    private fun mostrarDialogoAjusteSaldo() {

        val input = EditText(this)

        input.inputType = InputType.TYPE_CLASS_NUMBER

        input.hint = "Nuevo saldo en COP"

        AlertDialog.Builder(this)
            .setTitle("Ajustar saldo manualmente")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->

                val texto = input.text.toString()

                val nuevoSaldo = texto.toLongOrNull()

                if (nuevoSaldo == null) {

                    Toast.makeText(
                        this,
                        "Ingresa un valor numérico válido",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Almacenamiento.establecerSaldo(
                        this,
                        nuevoSaldo
                    )

                    cargarDatos()

                    Toast.makeText(
                        this,
                        "Saldo actualizado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
