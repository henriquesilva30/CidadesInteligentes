package ipvc.estg.cidadesinteligentes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cidadesinteligentes.adapters.notasAdapter
import ipvc.estg.room.entities.Notas
import ipvc.estg.room.viewModel.NotasViewModel


//teste merge
class MainActivity : AppCompatActivity() {


    private lateinit var notasViewModel: NotasViewModel
    private val newWordActivityRequestCode = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = notasAdapter(this,this)
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

    fun delete(id : Int?){
        notasViewModel.delete(id)
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

            else -> super.onOptionsItemSelected(item)
        }
    }
}


