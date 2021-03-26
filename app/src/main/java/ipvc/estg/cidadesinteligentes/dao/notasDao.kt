package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Notas

@Dao
interface notasDao {

    @Query("SELECT * from notas_table ORDER BY id ASC")
    fun getAllNotas(): LiveData<List<Notas>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Update
    suspend fun updateNotas(notas: Notas)

    @Query("DELETE FROM notas_table")
    suspend fun deleteAll()

    @Query("DELETE FROM notas_table where id ==:id")
    suspend fun delete(id: Int?)

}