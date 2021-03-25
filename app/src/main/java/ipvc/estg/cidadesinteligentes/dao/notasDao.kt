package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Notas

@Dao
interface notasDao {

    @Query("SELECT * from notas_table ORDER BY id ASC")
    fun getAllNotas(): LiveData<List<Notas>>

    @Query("SELECT * FROM notas_table WHERE descric == :descric")
    fun getNotasByDescric(descric: String): LiveData<List<Notas>>

    @Query("SELECT * FROM notas_table WHERE data == :data")
    fun getNotasFromData(data: String): LiveData<Notas>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notas: Notas)

    @Update
    suspend fun updateNotas(notas: Notas)

    @Query("DELETE FROM notas_table")
    suspend fun deleteAll()

    @Query("DELETE FROM notas_table where descric == :descric")
    suspend fun deleteByNotas(descric: String)

}