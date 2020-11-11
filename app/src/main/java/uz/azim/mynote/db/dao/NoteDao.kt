package uz.azim.mynote.db.dao

import androidx.room.*
import uz.azim.mynote.entity.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes")
    suspend fun getAll() : List<Note>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Int) : Note
}
