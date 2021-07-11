package ipvc.estg.cidadesinteligentes

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import ipvc.estg.cidadesinteligentes.api.EndPoints
import ipvc.estg.cidadesinteligentes.api.Markers
import ipvc.estg.cidadesinteligentes.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_ponto.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.*
import java.util.*

class PontoActivity : AppCompatActivity() {

    var longitude: Double? = null
    var latitude: Double? = null
    private var iduserM: Int? = null
    private var mLat: Double? = null
    private var mLng: Double? = null
    private var imagem: MultipartBody.Part? = null
    private var bmp: Bitmap? = null
    private var requestFile: RequestBody? = null
    private var fileToUpload: File? = null
    private lateinit var pontos: List<Markers>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    private lateinit var imageContainer: ImageView
    private lateinit var locationRequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto)

        val intentPonto: Bundle? = intent.extras
        iduserM = intentPonto?.getInt("id")
        mLat = intentPonto?.getDouble("lat")
        mLng = intentPonto?.getDouble("lng")
        imageContainer = findViewById(R.id.imageView2)



        createLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                latitude = mLat
                longitude = mLng
            }
        }

        button2.setOnClickListener {
            finish()
        }



        button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this@PontoActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            }
            if (ContextCompat.checkSelfPermission(
                    this@PontoActivity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 100)
            }

        }
        button4.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, 111)
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 111)
            }
        }

        button3.setOnClickListener {
            if (imageContainer == null || editTextTextMultiLine.text.isNullOrEmpty()) {
                if (editTextTextMultiLine.text.isNullOrEmpty()) {
                    editTextTextMultiLine.error = getString(R.string.emp_pass)
                }
                if (imageContainer == null) {
                    Toast.makeText(this@PontoActivity, "Imagem por inserir", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else {
                val desc = findViewById<EditText>(R.id.editTextTextMultiLine)
                val radioValue = findViewById<RadioGroup>(R.id.radioGroup)
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.addPonto(
                    1,
                    iduserM,
                    desc.text.toString(),
                    mLat,
                    mLng,
                    imageContainer.toString()
                )
                call.enqueue(object : retrofit2.Callback<Markers> {
                    override fun onResponse(call: Call<Markers>, response: Response<Markers>) {
                        if (response.isSuccessful) {
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Markers>, t: Throwable) {
                        Toast.makeText(
                            this@PontoActivity,
                            "Erro ao registar",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 100) {
            if (data != null) {
                bmp = data.extras?.get("data") as Bitmap
                val uri = bitmapToFile(bmp!!)
                imageContainer.setImageURI(uri)

                requestFile =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        fileToUpload!!
                    )

                // MultipartBody.Part is used to send also the actual file name
                imagem =
                    MultipartBody.Part.createFormData(
                        "image",
                        fileToUpload!!.name,
                        requestFile!!
                    )
            }
        }
        if (requestCode == 111) {
            if (data != null) {
                val inputStream: InputStream? = contentResolver.openInputStream(data.data!!)
                bmp = BitmapFactory.decodeStream(inputStream)
                val uri = bitmapToFile(bmp!!)
                imageContainer.setImageURI(uri)

                requestFile =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        fileToUpload!!
                    )

                // MultipartBody.Part is used to send also the actual file name
                imagem =
                    MultipartBody.Part.createFormData(
                        "image",
                        fileToUpload!!.name,
                        requestFile!!
                    )
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        //GET CONTEXT WRAPPER
        val wrapper = ContextWrapper(applicationContext)

        //Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        fileToUpload = file

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }


    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000 //5 em 5 segundos
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}