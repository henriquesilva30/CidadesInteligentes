package ipvc.estg.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas_table")

class Notas(
    // Int? = null so when creating instance id has not to be passed as argument
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "descric") val descric: String,
    @ColumnInfo(name = "data") val data: String,
    @ColumnInfo(name = "hora") val hora: String,
    @ColumnInfo(name = "local") val local: String



    )