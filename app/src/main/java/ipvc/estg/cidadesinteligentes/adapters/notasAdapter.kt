package ipvc.estg.cidadesinteligentes.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cidadesinteligentes.R
import ipvc.estg.room.entities.Notas

class notasAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<notasAdapter.NotasViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Notas>() // Cached copy of cities

    class NotasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notasItemView: TextView = itemView.findViewById(R.id.descricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = notas[position]
        holder.notasItemView.text = current.descric + ", "+current.local+", "+ current.data+", "+current.hora
    }

    internal fun setNotas(notas: List<Notas>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}