package ipvc.estg.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cidadesinteligentes.adapters.notasAdapter
import ipvc.estg.room.entities.Notas
import ipvc.estg.room.viewModel.NotasViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var notasViewModel: NotasViewModel
    private val newWordActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = notasAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, notasPessoais::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val pdescric = data?.getStringExtra(notasPessoais.EXTRA_REPLY_DESCRIC)
            val pdata = data?.getStringExtra(notasPessoais.EXTRA_REPLY_DATA)
            val phora = data?.getStringExtra(notasPessoais.EXTRA_REPLY_HORA)
            val plocal = data?.getStringExtra(notasPessoais.EXTRA_REPLY_LOCAL)



            if (pdescric!= null || pdata!=null || phora!=null || plocal !=null ) {
                val notas = Notas(descric= pdescric!!, data=pdata!! , hora=phora!!, local = plocal!! )
                notasViewModel.insert(notas)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.apagartudo -> {
                notasViewModel.deleteAll()
                true
            }

            R.id.cidadesPortugal -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = notasAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
                notasViewModel.getNotasByLocalizacao("Portugal").observe(this, Observer { notas ->
                    // Update the cached copy of the words in the adapter.
                    notas?.let { adapter.setNotas(it) }
                })

                true
            }

            R.id.todasCidades -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = notasAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
                notasViewModel.allNotas.observe(this, Observer { notas ->
                    // Update the cached copy of the words in the adapter.
                    notas?.let { adapter.setNotas(it) }
                })


                true
            }

            R.id.getCountryFromAveiro -> {
                notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
                notasViewModel.getCountryFromCity("Aveiro").observe(this, Observer { notas ->
                    Toast.makeText(this, notas.descric, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarAveiro -> {
                notasViewModel.deleteByCity("Aveiro")
                true
            }

            R.id.alterar -> {
                val notas = Notas(id = 1, descric = "xxx", data ="xxxx", hora = "hora", local = "xxx" )
                notasViewModel.updateNotas(notas)
                true
            }



            else -> super.onOptionsItemSelected(item)
        }


    }
}
