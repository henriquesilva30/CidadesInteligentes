package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.notasDao
import ipvc.estg.room.entities.Notas

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class notasRepository(private val notasDao: notasDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotas: LiveData<List<Notas>> = notasDao.getAllNotas()


    suspend fun insert(notas: Notas) {
        notasDao.insert(notas)
    }

    suspend fun deleteAll(){
        notasDao.deleteAll()
    }

    suspend fun delete(id: Int?){
        notasDao.delete(id)
    }

    suspend fun updateNotas(notas: Notas) {
        notasDao.updateNotas(notas)
    }


}