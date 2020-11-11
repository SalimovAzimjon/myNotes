package uz.azim.mynote.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.azim.mynote.db.converters.Converters
import uz.azim.mynote.db.dao.NoteDao
import uz.azim.mynote.entity.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NoteDB : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}