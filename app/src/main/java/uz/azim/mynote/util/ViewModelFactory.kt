package uz.azim.mynote.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.ui.edit.EditFragmentVM
import uz.azim.mynote.ui.note.NoteViewModel

class ViewModelFactory(private val repository: NoteRepository,private val note: Note? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NoteViewModel::class.java) -> NoteViewModel(repository)
                isAssignableFrom(EditFragmentVM::class.java) -> EditFragmentVM(repository,note)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}