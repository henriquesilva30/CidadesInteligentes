package ipvc.estg.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.cidadesinteligentes.adapters.*
import ipvc.estg.room.entities.Notas
import ipvc.estg.room.viewModel.NotasViewModel
import java.text.SimpleDateFormat
import java.util.*


class notasPessoais : AppCompatActivity() {

    private lateinit var descText: EditText
    private lateinit var dataText: TextView
    private lateinit var horaText: TextView
    private lateinit var localText: EditText
    private lateinit var notasViewModel: NotasViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_pessoais)

        val editLocal = intent.getStringExtra(LOCAL)
        val editDescricao = intent.getStringExtra(DESCRICAO)
        val getData = intent.getStringExtra(DATA)
        val getHora = intent.getStringExtra(HORA)


        findViewById<EditText>(R.id.add_descricao).setText(editDescricao)
        findViewById<EditText>(R.id.add_localizacao).setText(editLocal)
        findViewById<TextView>(R.id.data).setText(getData)
        findViewById<TextView>(R.id.hora).setText(getHora)

        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)

        descText = findViewById(R.id.add_descricao)
        dataText = findViewById(R.id.data)
        localText = findViewById(R.id.add_localizacao)
        horaText = findViewById(R.id.hora)





        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            var message = intent.getIntExtra(ID, 0)
            val replyIntent = Intent()
            if(TextUtils.isEmpty((descText.text)) || TextUtils.isEmpty((localText.text))){

                if(TextUtils.isEmpty((descText.text)) && !TextUtils.isEmpty((localText.text))){
                    descText.error = getString(R.string.aviso_desc)
                }
                if(!TextUtils.isEmpty((descText.text)) && TextUtils.isEmpty((localText.text))){
                    localText.error = getString(R.string.aviso_local)
                }
                if(TextUtils.isEmpty((descText.text)) && TextUtils.isEmpty((localText.text))){
                    descText.error = getString(R.string.aviso_desc)
                    localText.error = getString(R.string.aviso_local)
                }
            }

            else if (message != 0)
            {

                val nota = Notas(
                    id = message,
                    descric = descText.text.toString(),
                    local = localText.text.toString(),
                    data = dataText.text.toString(),
                    hora = horaText.text.toString())
                notasViewModel.updateNotas(nota)
                finish()

            }
            //commit 22121

            else {

                val CurrentTime: TextView = findViewById(R.id.data)
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
                val currentDateandTime: String = sdf.format(Date())
                CurrentTime.text = currentDateandTime

                replyIntent.putExtra(EXTRA_REPLY_DESCRIC, descText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DATA, CurrentTime.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_HORA, horaText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_LOCAL, localText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }



    }





    companion object {
        const val EXTRA_REPLY_DESCRIC = "com.example.android.descric"
        const val EXTRA_REPLY_DATA = "com.example.android.data"
        const val EXTRA_REPLY_HORA = "com.example.android.hora"
        const val EXTRA_REPLY_LOCAL = "com.example.android.local"



    }
}
