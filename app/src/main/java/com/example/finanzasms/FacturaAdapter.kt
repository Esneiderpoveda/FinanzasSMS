package com.example.finanzasms

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FacturaAdapter(
    private val facturas: MutableList<Factura>,
    private val onFacturaClick: (Factura) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    class FacturaViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        val imagenFactura: ImageView =
            view.findViewById(R.id.imagenFactura)

        val textNotaFactura: TextView =
            view.findViewById(R.id.textNotaFactura)

        val textFechaFactura: TextView =
            view.findViewById(R.id.textFechaFactura)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacturaViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_factura, parent, false)

        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FacturaViewHolder,
        position: Int
    ) {

        val factura = facturas[position]

        // Mostrar imagen
        holder.imagenFactura.setImageURI(
            Uri.parse(factura.imagenUri)
        )

        // Mostrar nota
        if (factura.nota.isBlank()) {
            holder.textNotaFactura.text = "Comprobante"
        } else {
            holder.textNotaFactura.text = factura.nota
        }

        // Convertir la fecha guardada a un formato legible
        val formatoFecha =
            SimpleDateFormat(
                "dd/MM/yyyy - HH:mm",
                Locale.getDefault()
            )

        holder.textFechaFactura.text =
            formatoFecha.format(Date(factura.fecha))

        // Detectar cuando tocamos una factura
        holder.itemView.setOnClickListener {
            onFacturaClick(factura)
        }
    }

    override fun getItemCount(): Int {
        return facturas.size
    }
}