
package ipvc.estg.cidadesinteligentes

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.cidadesinteligentes.api.EndPoints
import ipvc.estg.cidadesinteligentes.api.ServiceBuilder
import ipvc.estg.cidadesinteligentes.api.Markers
import kotlinx.android.synthetic.main.custom_info_window.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pontos: List<Markers>
    private var userM: Int? = null
    private var pontoID: String = ""
    private lateinit var latLng: TextView
    private var pontoImgTxt: String = ""
    private lateinit var pontoImg: ImageView
    private var pontoData: String = ""
    private lateinit var pontoDesc: TextView
    private lateinit var pontoTipo: TextView
    private var linkImg: String = ""
    private lateinit var lastlocation: Location
    private lateinit var fusedlocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var id: Any? = null

        fusedlocationClient = LocationServices.getFusedLocationProviderClient(this)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.ofShared), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            id = sharedPref.all[getString(R.string.id)]
        }


        val adicionarMarca = findViewById<FloatingActionButton>(R.id.addmarco)

        adicionarMarca.setOnClickListener {
            val intent = Intent(this@MapsActivity, PontoActivity::class.java)
            intent.putExtra(userMap, userM)
            startActivity(intent)
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        //val rua = LatLng(41.698276, -8.8470264)
        // mMap.addMarker(MarkerOptions().position(rua).title("estatico"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(rua))
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rua, 14f))
        setUpMap()

    }

    fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MapsActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1000
            )
            return
        }else {
            mMap.isMyLocationEnabled = true

            fusedlocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastlocation = location
                    Toast.makeText(this@MapsActivity, userM.toString(), Toast.LENGTH_SHORT)
                        .show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.ofShared), Context.MODE_PRIVATE
                )
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.onShared), false)
                    putInt(getString(R.string.tlm), 0)
                    putInt(getString(R.string.id), 0)
                    commit()
                }

                var intent = Intent(this, firstActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.nav_notas -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onBackPressed() {}

    override fun onResume() {
        super.onResume()

        val intent: Bundle? = intent.extras
        userM = intent?.getInt(firstActivity.userMap)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getPontos()
        var position: LatLng

        call.enqueue(object : Callback<List<Markers>> {
            override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {
                if (response.isSuccessful) {
                    pontos = response.body()!!

                    for (ponto in pontos) {
                        position = LatLng(
                            ponto.lat.toString().toDouble(),
                            ponto.lng.toString().toDouble()
                        )

                        // Setting a custom info window adapter for the google map
                        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                            // Use default InfoWindow frame
                            override fun getInfoWindow(arg0: Marker): View? {
                                return null
                            }

                            // Defines the contents of the InfoWindow
                            override fun getInfoContents(arg0: Marker): View {
                                var v: View? = null
                                try {
                                    // Getting view from the layout file info_window_layout
                                    v = layoutInflater.inflate(
                                        R.layout.custom_info_window,
                                        null
                                    )
                                    // Getting reference to the TextView to set latitude
                                    pontoDesc =
                                        v!!.findViewById<View>(R.id.descdetalhes) as TextView
                                    pontoDesc.text = arg0.title.substringAfter(" -")
                                    pontoTipo =
                                        v.findViewById<View>(R.id.tipodetalhes) as TextView
                                    pontoTipo.text = arg0.snippet
                                    latLng =
                                        v.findViewById<View>(R.id.latdetalhes) as TextView
                                    latLng.text = arg0.position.toString()
                                    pontoImg =
                                        v.findViewById<View>(R.id.imgdetalhes) as ImageView
                                    pontoID = arg0.title.substringBefore(" -")
                                    pontoImgTxt = arg0.title.substringBefore(" -")


                                    Glide.with(this@MapsActivity)
                                        .load(arg0.title.substringBefore(" -")).into(pontoImg)


                                } catch (ev: Exception) {
                                    print(ev.message)
                                }
                                return v!!
                            }
                        })

                        if (ponto.id_utilizador == userM) {

                            mMap.addMarker(
                                MarkerOptions().position(position).title(
                                    "" + ponto.img + " - " +
                                            ponto.descricao
                                ).snippet("" + ponto.id_tipo_nota).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_ORANGE
                                    )
                                )
                            )
                        } else {

                            mMap.addMarker(
                                MarkerOptions().position(position).title(
                                    "" + ponto.img + " - " +
                                            ponto.descricao
                                ).snippet("" + ponto.id_tipo_nota).icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_YELLOW
                                    )
                                )
                            )
                        }

                        mMap.setOnInfoWindowClickListener() {
                            val datelhesPonto =
                                Intent(this@MapsActivity, detalhesPontos::class.java)
                            datelhesPonto.putExtra(imgTxt, pontoImgTxt)
                            datelhesPonto.putExtra(idUser, userM)
                            datelhesPonto.putExtra(idPonto, pontoID)
                            datelhesPonto.putExtra(idUserponto, ponto.id)
                            datelhesPonto.putExtra(UserPonto, ponto.id_utilizador)
                            datelhesPonto.putExtra(dataPonto, ponto.data)
                            datelhesPonto.putExtra(descPonto, pontoDesc.text.toString())
                            datelhesPonto.putExtra(
                                categoriaPonto,
                                pontoTipo.text.toString()
                            )
                            datelhesPonto.putExtra(latlng, latLng.text.toString())
                            startActivity(datelhesPonto)
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }


    companion object {
        const val userMap = "com.example.android.usermap"
        const val idUser = "com.example.android.iduser"
        const val imgTxt = "com.example.android.imgTxt"
        const val idPonto = "com.example.android.idponto"
        const val idUserponto = "com.example.android.iduseruponto"
        const val latlng = "com.example.android.latlng"
        const val descPonto = "com.example.android.descponto"
        const val dataPonto = "com.example.android.dataPonto"
        const val UserPonto = "com.example.android.UserPonto"
        const val categoriaPonto = "com.example.android.categoriaponto"
    }

}
