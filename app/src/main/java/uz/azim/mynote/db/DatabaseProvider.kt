package uz.azim.mynote.db

import androidx.room.Room
import uz.azim.mynote.App
import uz.azim.mynote.db.dao.NoteDao

object DatabaseProvider {
    val noteDao: NoteDao = getDatabaseDao()

    private fun getDatabaseDao(): NoteDao {
        return synchronized(this) {
            val instance = Room.databaseBuilder(
                App.appInstance,
                NoteDB::class.java,
                "my_notes_db"
            )
                .fallbackToDestructiveMigration()
                .build()
            instance.noteDao()
        }
    }
}