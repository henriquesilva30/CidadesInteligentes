package ipvc.estg.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.room.dao.notasDao
import ipvc.estg.room.entities.Notas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the City class

// Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, for a real app, you must implement a migration strategy.

@Database(entities = arrayOf(Notas::class), version = 8, exportSchema = false)
public abstract class notasDB : RoomDatabase() {

    abstract fun notasDao(): notasDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var notasDao = database.notasDao()

                /*    // Delete all content here.
                    notasDao.deleteAll()

                    // Add sample cities.
                    var notas = Notas(1,  "Estudar","20/03/2021","18:16","Rua Eurico Santos nº4")
                    notasDao.insert(notas)
                    notas = Notas(2, "Porto","20/03/2021","18:16","Praça Adelino Almeira")
                    notasDao.insert(notas)
                    notas = Notas(3, "Aveiro","20/03/2021","18:16","Senhor do Socorro")
                    notasDao.insert(notas)
                    notas = Notas(4, "Portugal","13/03/2021","18:17","Rua Comendador Araujo")
                    notasDao.insert(notas)
                    notas = Notas(5, "Tarefas","20/03/2021","18:16","Rua da Bonança nº108")
                    notasDao.insert(notas)

*/
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: notasDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): notasDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    notasDB::class.java,
                    "notas_database"
                )
                //estratégia de destruição
                //.fallbackToDestructiveMigration()
                .addCallback(WordDatabaseCallback(scope))
                .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}