package ipvc.estg.cidadesinteligentes.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cidadesinteligentes.MainActivity
import ipvc.estg.cidadesinteligentes.R
import ipvc.estg.cidadesinteligentes.notasPessoais
import ipvc.estg.room.entities.Notas
import ipvc.estg.room.viewModel.NotasViewModel

const val ID = "ID"
    const val DESCRICAO = "DRESCRICAO"
    const val LOCAL = "LOCAL"

    private lateinit var notasViewModel: NotasViewModel



class notasAdapter internal constructor(
    context: Context, private val callbackInterface: MainActivity
) : RecyclerView.Adapter<notasAdapter.NotasViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Notas>() // Cached copy of cities

    interface CallbackInterface {
        fun passResultCallback(id: Int?)
    }

    class NotasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notasItemView: TextView = itemView.findViewById(R.id.descricao)
        val local: TextView = itemView.findViewById(R.id.localizacao)
        val edit : LinearLayout = itemView.findViewById(R.id.layoutEdit)
        val delete : ImageView = itemView.findViewById(R.id.Imagedelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotasViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        val current = notas[position]
        holder.notasItemView.text = current.descric
        holder.local.text = current.local
        //  holder.hora.text = current.hora
       // holder.data.text = current.data
        val id: Int? = current.id

        //merge current
        holder.edit.setOnClickListener {
            val context = holder.notasItemView.context
            val titl = holder.local.text.toString()
            val desc = holder.notasItemView.text.toString()

            val intent = Intent(context, notasPessoais::class.java).apply {
                putExtra(LOCAL, titl)
                putExtra(DESCRICAO, desc)
                putExtra(ID, id)
            }
            context.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            callbackInterface.delete(current.id)
        }



    }
        internal fun setNotas(notas: List<Notas>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}