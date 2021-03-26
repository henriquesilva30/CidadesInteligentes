package ipvc.estg.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.room.db.notasRepository
import ipvc.estg.room.db.notasDB
import ipvc.estg.room.entities.Notas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: notasRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotas: LiveData<List<Notas>>

    init {
        val notasDao = notasDB.getDatabase(application, viewModelScope).notasDao()
        repository = notasRepository(notasDao)
        allNotas = repository.allNotas
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(notas: Notas) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(notas)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by notas
    fun delete(id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(id)
    }



    fun updateNotas(notas: Notas) = viewModelScope.launch {
        repository.updateNotas(notas)
    }


}