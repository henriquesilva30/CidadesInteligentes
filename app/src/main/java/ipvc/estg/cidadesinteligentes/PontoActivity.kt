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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_ponto.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class PontoActivity : AppCompatActivity() {

    var longitude: Double? = null
    var latitude: Double? = null
    private var iduserM: Int? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var lastLocation: Location
    private lateinit var imageView2: ImageView





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ponto)

        val intentPonto: Bundle? = intent.extras
        iduserM = intentPonto?.getInt(MapsActivity.userMap)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
        }


        imageView2 = findViewById(R.id.imageView2)



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
                startActivityForResult(intent, 123)
            }

        }
        button4.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,456)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode==123){
            var bmp = data?.extras?.get("data") as Bitmap
            imageView2.setImageBitmap(bmp)
        }else if (requestCode == 456){
            imageView2.setImageURI(data?.data)
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}