package ipvc.estg.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class notasPessoais : AppCompatActivity() {

    private lateinit var descText: EditText
    private lateinit var dataText: TextView
    private lateinit var horaText: TextView
    private lateinit var localText: EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notas_pessoais)

        descText = findViewById(R.id.add_descricao)
        dataText = findViewById(R.id.data)
        localText = findViewById(R.id.add_localizacao)
        horaText = findViewById(R.id.hora)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(descText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_DESCRIC, descText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DATA, dataText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_HORA, horaText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_LOCAL, localText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_DESCRIC = "com.example.android.descric"
        const val EXTRA_REPLY_DATA = "com.example.android.data"
        const val EXTRA_REPLY_HORA = "com.example.android.hora"
        const val EXTRA_REPLY_LOCAL = "com.example.android.local"



    }
}
