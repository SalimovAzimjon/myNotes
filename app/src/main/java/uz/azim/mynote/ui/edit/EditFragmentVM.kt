package uz.azim.mynote.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.util.Resource
import uz.azim.mynote.util.Result

class EditFragmentVM(private val repository: NoteRepository) : ViewModel() {
    private val _saveLiveData = MutableLiveData<Result>()
    val saveLiveData: LiveData<Result> = _saveLiveData

    @ExperimentalCoroutinesApi
    fun saveNote(note: Note) = viewModelScope.launch {
        repository.saveNote(note)
            .onStart {
                _saveLiveData.postValue(Result.LOADING)
            }
            .catch {
                _saveLiveData.postValue(Result.ERROR)
            }
            .collect {
                _saveLiveData.postValue(Result.SUCCESS)
            }
    }
}