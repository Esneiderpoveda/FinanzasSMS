
package com.example.finanzasms

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.io.File
import androidx.core.content.FileProvider

class FotosActivity : AppCompatActivity() {

    // Lista de facturas guardadas
    private val listaFacturas = mutableListOf<Factura>()

    // Adaptador de la lista
    private lateinit var facturaAdapter: FacturaAdapter

    private var fotoTemporal: File? = null

    private val tomarFoto =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { fotografiaGuardada ->

            if (fotografiaGuardada) {

                val archivo = fotoTemporal

                if (archivo != null) {
                    mostrarFormularioNota(
                        Uri.fromFile(archivo)
                    )
                }

            } else {

                Toast.makeText(
                    this,
                    "No se tomó ninguna fotografía",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    // Seleccionar fotografía desde la galería
    private val seleccionarImagen =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                mostrarFormularioNota(uri)
            }
        }

    // --------------------------------------------------
    // INICIO DE LA PANTALLA
    // --------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fotos)


        // MENÚ HAMBURGUESA DE COMPROBANTES
        val botonMenuFotos = findViewById<TextView>(
            R.id.botonMenuFotos
        )

        botonMenuFotos.setOnClickListener {
            MenuNavegacion.mostrarMenu(this, botonMenuFotos)
        }

        // Configurar RecyclerView
        val recyclerFacturas =
            findViewById<RecyclerView>(
                R.id.recyclerFacturas
            )

        // Crear adaptador
        facturaAdapter = FacturaAdapter(listaFacturas) { factura ->

            // Abrir detalle al tocar una factura
            mostrarDetalleFactura(factura)
        }

        recyclerFacturas.layoutManager =
            LinearLayoutManager(this)

        recyclerFacturas.adapter = facturaAdapter

        // Recuperar facturas guardadas
        listaFacturas.clear()

        listaFacturas.addAll(
            AlmacenamientoFacturas.cargarFacturas(this)
        )

        facturaAdapter.notifyDataSetChanged()

        // Actualizar mensaje de lista vacía
        actualizarMensajeListaVacia()

        // Botón volver
        val botonVolver =
            findViewById<TextView>(
                R.id.botonVolver
            )

        botonVolver.setOnClickListener {
            finish()
        }

        // Botón agregar factura
        val botonAgregarFactura =
            findViewById<Button>(
                R.id.botonAgregarFactura
            )

        botonAgregarFactura.setOnClickListener {
            mostrarOpcionesFactura()
        }
    }

    // --------------------------------------------------
    // OPCIONES PARA AGREGAR COMPROBANTE
    // --------------------------------------------------

    private fun mostrarOpcionesFactura() {

        val opciones = arrayOf(
            "📷 Tomar foto",
            "🖼️ Elegir de galería"
        )

        AlertDialog.Builder(this)
            .setTitle("Agregar comprobante")
            .setItems(opciones) { _, opcion ->

                when (opcion) {

                    0 -> {
                        abrirCamara()
                    }

                    1 -> {
                        seleccionarImagen.launch("image/*")
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    // --------------------------------------------------
    // ABRIR CÁMARA DE LA TABLET
    // --------------------------------------------------

    private fun abrirCamara() {

        try {

            // Crear archivo temporal para la fotografía
            val archivo = File.createTempFile(
                "comprobante_",
                ".jpg",
                cacheDir
            )

            // Recordar dónde se guardará la fotografía
            fotoTemporal = archivo

            // Crear URI segura para la cámara
            val uriFoto = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                archivo
            )

            // Abrir la cámara
            tomarFoto.launch(uriFoto)

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Error al abrir la cámara: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun mostrarFormularioNota(uri: Uri) {

        val campoNota = EditText(this)

        campoNota.hint = "Ej: Compra supermercado"

        campoNota.setPadding(
            40,
            30,
            40,
            30
        )

        AlertDialog.Builder(this)
            .setTitle("Agregar nota")
            .setMessage(
                "Puedes escribir una descripción para este comprobante. " +
                        "Si la dejas vacía, se guardará únicamente la fecha en que fue agregado."
            )
            .setView(campoNota)
            .setPositiveButton("Guardar") { _, _ ->

                // Obtener nota
                val nota = campoNota.text
                    .toString()
                    .trim()

                try {

                    // Guardar copia de la imagen
                    val imagenGuardada =
                        AlmacenamientoFacturas.guardarImagen(
                            this,
                            uri
                        )

                    // Registrar fecha actual
                    val fechaActual =
                        System.currentTimeMillis()

                    // Crear factura
                    val nuevaFactura = Factura(
                        id = fechaActual,
                        imagenUri = imagenGuardada,
                        nota = nota,
                        fecha = fechaActual
                    )

                    // Agregar factura al principio
                    listaFacturas.add(
                        0,
                        nuevaFactura
                    )

                    // Guardar datos permanentemente
                    AlmacenamientoFacturas.guardarFacturas(
                        this,
                        listaFacturas
                    )

                    // Actualizar lista visual
                    facturaAdapter.notifyItemInserted(0)

                    findViewById<RecyclerView>(
                        R.id.recyclerFacturas
                    ).scrollToPosition(0)

                    // Ocultar mensaje de lista vacía
                    actualizarMensajeListaVacia()

                    Toast.makeText(
                        this,
                        "Comprobante guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {

                    Toast.makeText(
                        this,
                        "Error al guardar el comprobante",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // --------------------------------------------------
    // ACTUALIZAR MENSAJE DE LISTA VACÍA
    // --------------------------------------------------

    private fun actualizarMensajeListaVacia() {

        val textSinFacturas =
            findViewById<TextView>(
                R.id.textSinFacturas
            )

        if (listaFacturas.isEmpty()) {

            textSinFacturas.visibility = View.VISIBLE

        } else {

            textSinFacturas.visibility = View.GONE
        }
    }

    // --------------------------------------------------
    // MOSTRAR DETALLE DE UNA FACTURA
    // --------------------------------------------------

    private fun mostrarDetalleFactura(factura: Factura) {

        // Contenedor vertical
        val contenedor = LinearLayout(this).apply {

            orientation = LinearLayout.VERTICAL

            setPadding(
                40,
                24,
                40,
                24
            )
        }

        // Fotografía ampliada
        val imagen = ImageView(this).apply {

            setImageURI(
                Uri.parse(factura.imagenUri)
            )

            adjustViewBounds = true

            scaleType =
                ImageView.ScaleType.FIT_CENTER

            layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    600
                )
        }

        // Descripción de la factura
        val descripcion = TextView(this).apply {

            text = if (factura.nota.isBlank()) {
                "Sin descripción"
            } else {
                factura.nota
            }

            textSize = 17f

            setPadding(
                0,
                24,
                0,
                12
            )
        }

        // Formatear fecha
        val formatoFecha = SimpleDateFormat(
            "dd/MM/yyyy - HH:mm",
            Locale.getDefault()
        )

        // Mostrar fecha
        val fecha = TextView(this).apply {

            text = "Agregado el: ${
                formatoFecha.format(
                    Date(factura.fecha)
                )
            }"

            textSize = 14f
        }

        // Añadir elementos al contenedor
        contenedor.addView(imagen)
        contenedor.addView(descripcion)
        contenedor.addView(fecha)

        // Permitir desplazamiento si el contenido es largo
        val scrollView = ScrollView(this)

        scrollView.addView(contenedor)

        // Mostrar ventana con el detalle
        AlertDialog.Builder(this)
            .setTitle("Detalle del comprobante")
            .setView(scrollView)
            .setPositiveButton("Cerrar", null)
            .setNegativeButton("Eliminar") { _, _ ->

                AlertDialog.Builder(this)
                    .setTitle("Eliminar comprobante")
                    .setMessage(
                        "¿Estás seguro de que deseas eliminar este comprobante? " +
                                "Esta acción no se puede deshacer."
                    )
                    .setPositiveButton("Sí, eliminar") { _, _ ->

                        val posicion = listaFacturas.indexOfFirst {
                            it.id == factura.id
                        }

                        if (posicion != -1) {

                            // Obtener la dirección de la imagen guardada
                            val uriImagen = Uri.parse(factura.imagenUri)

                            // Eliminar el archivo si pertenece al almacenamiento privado
                            if (uriImagen.scheme == "file") {

                                val ruta = uriImagen.path

                                if (ruta != null) {

                                    val archivo = File(ruta)
                                    val carpetaFacturas = File(filesDir, "facturas")

                                    if (
                                        archivo.canonicalFile.parentFile ==
                                        carpetaFacturas.canonicalFile
                                    ) {
                                        archivo.delete()
                                    }
                                }
                            }

                            // Eliminar la factura de la lista
                            listaFacturas.removeAt(posicion)

                            // Actualizar los datos guardados
                            AlmacenamientoFacturas.guardarFacturas(
                                this,
                                listaFacturas
                            )

                            facturaAdapter.notifyItemRemoved(posicion)

                            actualizarMensajeListaVacia()

                            Toast.makeText(
                                this,
                                "Comprobante eliminado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()

            }

            .show()

    }

}