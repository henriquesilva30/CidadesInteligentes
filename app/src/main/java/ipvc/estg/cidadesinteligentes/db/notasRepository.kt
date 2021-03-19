package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.notasDao
import ipvc.estg.room.entities.Notas

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class notasRepository(private val notasDao: notasDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCities: LiveData<List<Notas>> = notasDao.getAllCities()

    fun getCitiesByCountry(country: String): LiveData<List<Notas>> {
        return notasDao.getCitiesByCountry(country)
    }

    fun getCountryFromCity(city: String): LiveData<Notas> {
        return notasDao.getCountryFromCity(city)
    }

    suspend fun insert(notas: Notas) {
        notasDao.insert(notas)
    }

    suspend fun deleteAll(){
        notasDao.deleteAll()
    }

    suspend fun deleteByCity(city: String){
        notasDao.deleteByCity(city)
    }

    suspend fun updateCity(notas: Notas) {
        notasDao.updateCity(notas)
    }

    suspend fun updateCountryFromCity(city: String, country: String){
        notasDao.updateCountryFromCity(city, country)
    }
}