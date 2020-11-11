package uz.azim.mynote.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uz.azim.mynote.App
import uz.azim.mynote.db.dao.NoteDao
import uz.azim.mynote.entity.Note

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {
    private lateinit var database: NoteDB
    private lateinit var dao: NoteDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(App.appInstance, NoteDB::class.java)
            .allowMainThreadQueries().build()
        dao = database.noteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNote() = runBlockingTest {
        val note = Note("Titlw","dsds","asdas",false)
        dao.insert(note)

        val allNotes = dao.getAll()

        assertThat(allNotes).contains(note)
    }

    @Test
    fun deleteNote() = runBlockingTest {
        val note = Note("Titlw","dsds","asdas",false)
        dao.insert(note)
        dao.delete(note)

        val allNotes = dao.getAll()

        assertThat(allNotes).doesNotContain(note)
    }
}