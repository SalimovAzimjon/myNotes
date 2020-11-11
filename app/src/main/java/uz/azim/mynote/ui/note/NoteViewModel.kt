package uz.azim.mynote.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.util.Resource

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableLiveData<Resource<ArrayList<Note>>>()
    val note: LiveData<Resource<ArrayList<Note>>> = _notes



    fun getNotes() = viewModelScope.launch {
        repository.getNotes()
            .onStart {
                _notes.postValue(Resource.Loading())
            }
            .catch {
                _notes.postValue(Resource.Error(it.message.toString()))
            }
            .collect {
                _notes.postValue(Resource.Success(it))
            }
    }
}