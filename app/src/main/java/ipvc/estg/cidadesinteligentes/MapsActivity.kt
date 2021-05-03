package ipvc.estg.cidadesinteligentes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.cidadesinteligentes.api.EndPoints
import ipvc.estg.cidadesinteligentes.api.ServiceBuilder
import ipvc.estg.cidadesinteligentes.api.nota
import ipvc.estg.cidadesinteligentes.api.user
import kotlinx.android.synthetic.main.activity_first.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var notas: List<nota>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var id: Any? = null;

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.ofShared), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            id = sharedPref.all[getString(R.string.id)]
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getPontos()
        var position: LatLng
//
        call.enqueue(object : Callback<List<nota>> {
            override fun onResponse(call: Call<List<nota>>, response: Response<List<nota>>) {
                if (response.isSuccessful) {
                    for (user in notas) {
                        position = LatLng(
                            user.geo.lat.toString().toDouble(),
                            user.geo.lng.toString().toDouble()
                        )
                        mMap.addMarker(
                            MarkerOptions().position(position).title(user.desc + " - " + user.local)
                        )
                    }
                }
            }
            override fun onFailure(call: Call<List<nota>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
        val rua = LatLng(41.698276, -8.8470264)
        // mMap.addMarker(MarkerOptions().position(rua).title("estatico"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rua))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rua, 14f))

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
    override fun onBackPressed() {             }

}
