package uz.azim.mynote.ui.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.azim.mynote.TAG
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.util.Resource
import uz.azim.mynote.util.Result

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    private val _notes = MutableLiveData<Resource<ArrayList<Note>>>()
    val note: LiveData<Resource<ArrayList<Note>>> = _notes

    private val _updateLiveData = MutableLiveData<Result>()
    val updateLiveData: LiveData<Result> = _updateLiveData


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

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
            .onStart {
                _updateLiveData.postValue(Result.LOADING)
            }
            .catch {
                _updateLiveData.postValue(Result.ERROR)
            }
            .collect {
                _updateLiveData.postValue(Result.SUCCESS)
            }
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
            .onStart {
                _updateLiveData.postValue(Result.LOADING)
            }
            .catch {
                _updateLiveData.postValue(Result.ERROR)
            }
            .collect {
                _updateLiveData.postValue(Result.SUCCESS)
            }
    }

    fun nukeTable() = viewModelScope.launch {
        repository.nukeTable()
            .onStart {
                _updateLiveData.postValue(Result.LOADING)
            }
            .catch {
                _updateLiveData.postValue(Result.ERROR)
            }
            .collect {
                _updateLiveData.postValue(Result.SUCCESS)
            }
    }
}