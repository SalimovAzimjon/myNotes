package uz.azim.mynote.repository

import kotlinx.coroutines.flow.flow
import uz.azim.mynote.db.DatabaseProvider
import uz.azim.mynote.db.dao.NoteDao
import uz.azim.mynote.entity.Note

class NoteRepository {
    private val dao: NoteDao = DatabaseProvider.noteDao

    suspend fun getNotes() = flow {
        emit(dao.getAll() as ArrayList<Note>)
    }

    suspend fun saveNote(note: Note) = flow {
        emit(dao.insert(note))
    }

    suspend fun updateNote(note: Note) = flow {
        emit(dao.update(note))
    }

    suspend fun deleteNote(note: Note) = flow {
        emit(dao.delete(note))
    }

    suspend fun nukeTable() = flow {
        emit(dao.nukeTable())
    }
}