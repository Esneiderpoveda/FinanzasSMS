package com.example.finanzasms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter simple para mostrar la lista de transacciones detectadas
 * en un RecyclerView.
 */
class TransaccionAdapter(
    private var transacciones: List<Transaccion>
) : RecyclerView.Adapter<TransaccionAdapter.TransaccionViewHolder>() {

    private val formatoMoneda: NumberFormat =
        NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    class TransaccionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDescripcion: TextView = itemView.findViewById(R.id.textDescripcion)
        val textRemitente: TextView = itemView.findViewById(R.id.textRemitente)
        val textMonto: TextView = itemView.findViewById(R.id.textMonto)
        val textFecha: TextView = itemView.findViewById(R.id.textFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaccionViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaccion, parent, false)
        return TransaccionViewHolder(vista)
    }

    override fun onBindViewHolder(holder: TransaccionViewHolder, position: Int) {
        val transaccion = transacciones[position]


        val descripcionLimpia = transaccion.descripcion
            .replace(Regex("""\s*\*/=\S+.*$"""), "")
            .trim()

        holder.textDescripcion.text = descripcionLimpia

        holder.textRemitente.text = transaccion.remitente
        holder.textFecha.text = formatoFecha.format(Date(transaccion.fecha))

        val signo = if (transaccion.tipo == "INGRESO") "+ " else "- "
        holder.textMonto.text = signo + formatoMoneda.format(transaccion.monto)

        val colorTexto = if (transaccion.tipo == "INGRESO") {
            0xFF2E7D32.toInt() // verde
        } else {
            0xFFC62828.toInt() // rojo
        }
        holder.textMonto.setTextColor(colorTexto)
    }

    override fun getItemCount(): Int = transacciones.size

    /**
     * Reemplaza el conjunto de datos actual y refresca la lista completa.
     */
    fun actualizarDatos(nuevaLista: List<Transaccion>) {
        transacciones = nuevaLista
        notifyDataSetChanged()
    }
}
