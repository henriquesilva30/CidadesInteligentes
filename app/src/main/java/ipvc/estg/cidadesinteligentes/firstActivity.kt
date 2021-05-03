package ipvc.estg.cidadesinteligentes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.cidadesinteligentes.api.EndPoints
import ipvc.estg.cidadesinteligentes.api.OutputPost
import ipvc.estg.cidadesinteligentes.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_first.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class firstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.ofShared), Context.MODE_PRIVATE)

        if (sharedPref != null) {
            if (sharedPref.all[getString(R.string.onShared)] == true) {
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
          /*  if (sharedPref.all[getString(R.string.ofShared)] == true) {
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)

            }*/
        }

//
        notas.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }



    fun login(view: View) {

        var nr = findViewById<EditText>(R.id.user)
        var pass = findViewById<EditText>(R.id.passe)
        val intent = Intent(this, MapsActivity::class.java)


        if(nr.text.isNullOrEmpty() || pass.text.isNullOrEmpty()){
            if(nr.text.isNullOrEmpty()){
                nr.error = getString(R.string.emp_pass)
            }
            if(pass.text.isNullOrEmpty()){
                pass.error = getString(R.string.emp_pass)
            }
        }
        else{
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.postLog(nr.text.toString(), pass.text.toString())
            call.enqueue(object : Callback<List<OutputPost>>{
                override fun onResponse(call: Call<List<OutputPost>>, response: Response<List<OutputPost>>) {
                    if (response.isSuccessful){
                        for(OutputPost in response.body()!!){
                            val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.ofShared), Context.MODE_PRIVATE)
                            with(sharedPref.edit()){
                                putBoolean(getString(R.string.onShared), true)
                                putInt(getString(R.string.tlm), OutputPost.telemovel)
                                putInt(getString(R.string.id), OutputPost.id)
                                commit()
                            }
                        }
                        startActivity(intent)
                    }
                }
                override fun onFailure(call: Call<List<OutputPost>>, t: Throwable) {
                    Toast.makeText(this@firstActivity, getString(R.string.erro_campos), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    override fun onBackPressed() {             }

}


