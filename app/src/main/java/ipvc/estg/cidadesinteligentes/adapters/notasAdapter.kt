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
) : RecyclerView.Adapter<notasAdapter.CityViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cities = emptyList<Notas>() // Cached copy of cities

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val current = cities[position]
        holder.cityItemView.text = current.id.toString() + " - " + current.city + "-" + current.country
    }

    internal fun setCities(notas: List<Notas>) {
        this.cities = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = cities.size
}