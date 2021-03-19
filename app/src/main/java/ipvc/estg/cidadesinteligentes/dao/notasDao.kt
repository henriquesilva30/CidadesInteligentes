package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Notas

@Dao
interface notasDao {

    @Query("SELECT * from city_table ORDER BY city ASC")
    fun getAllCities(): LiveData<List<Notas>>

    @Query("SELECT * FROM city_table WHERE country == :country")
    fun getCitiesByCountry(country: String): LiveData<List<Notas>>

    @Query("SELECT * FROM city_table WHERE city == :city")
    fun getCountryFromCity(city: String): LiveData<Notas>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Update
    suspend fun updateCity(notas: Notas)

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()

    @Query("DELETE FROM city_table where city == :city")
    suspend fun deleteByCity(city: String)

    @Query("UPDATE city_table SET country=:country WHERE city == :city")
    suspend fun updateCountryFromCity(city: String, country: String)
}