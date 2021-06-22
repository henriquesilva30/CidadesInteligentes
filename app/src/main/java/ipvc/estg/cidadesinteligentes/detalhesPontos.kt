package ipvc.estg.cidadesinteligentes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detalhes_pontos.*
import kotlinx.android.synthetic.main.custom_info_window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class detalhesPontos : AppCompatActivity() {

    private lateinit var idPonto: String
    private lateinit var popupDesc: String
    private lateinit var popupTipo: String
    private lateinit var popupLatlng: String
    private lateinit var popupData: String
    private var idlogin: Int = 0
    private var iduserreport: Int = 0
    private val CLIENT_ID = "ae8b97176cae1d4"
    private lateinit var imgPonto: ImageView
    private lateinit var descPonto: TextView
    private lateinit var tipoPonto: TextView
    private lateinit var dataPonto: TextView
    private lateinit var LatlngPonto: TextView
    private lateinit var btPost: Button
    private lateinit var btDel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_detalhes_pontos)

        imgPonto = findViewById(R.id.img)
        descPonto = findViewById(R.id.desc)
        tipoPonto = findViewById(R.id.t_a)
        LatlngPonto = findViewById(R.id.lat)
        dataPonto = findViewById(R.id.dataPonto)

        val pontoSelec: Bundle? = intent.extras
        idPonto = pontoSelec?.getString(MapsActivity.idPonto).toString()
        popupDesc =
            pontoSelec?.getString(MapsActivity.descPonto).toString()
        popupTipo = pontoSelec?.getString(MapsActivity.categoriaPonto).toString()
        popupLatlng =
            pontoSelec?.getString(MapsActivity.latlng).toString()
        idlogin = pontoSelec?.getInt(MapsActivity.idUser)!!
        popupData = pontoSelec?.getString(MapsActivity.dataPonto).toString()

        descPonto.text = popupDesc
        tipoPonto.text = popupTipo
        LatlngPonto.text = popupLatlng
        dataPonto.text = popupData

        Glide.with(this@detalhesPontos).load(pontoSelec?.getString(MapsActivity.imgTxt))
            .into(imgPonto)

    }

    private fun uploadImageToImgur(image: Bitmap) {
        getBase64Image(image, complete = { base64Image ->
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL("https://30joelsilva.imgur.com/all/")
//https://api.imgur.com/3/image
                val boundary = "Boundary-${System.currentTimeMillis()}"

                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.setRequestProperty("Authorization", "Client-ID $CLIENT_ID")
                httpsURLConnection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true

                var body = ""
                body += "--$boundary\r\n"
                body += "Content-Disposition:form-data; name=\"image\""
                body += "\r\n\r\n$base64Image\r\n"
                body += "--$boundary--\r\n"


                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(body)
                    outputStreamWriter.flush()
                }

                // ...

                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                val data = jsonObject.getJSONObject("data")

                //            Log.d("TAG", "Link is : ${data.getString("link")}")
                //    linkImg = data.getString("link")
            }
        })
    }

    private fun getBase64Image(image: Bitmap, complete: (String) -> Unit) {
        GlobalScope.launch {
            val outputStream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val b = outputStream.toByteArray()
            complete(Base64.encodeToString(b, Base64.DEFAULT))
        }
    }
}


