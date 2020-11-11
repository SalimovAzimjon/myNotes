package uz.azim.mynote.ui.edit

import android.net.Uri
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
import uz.azim.mynote.util.Result

class EditFragmentVM(private val repository: NoteRepository, note: Note?) : ViewModel() {
    val images = ArrayList<Uri>()

    private val _saveLiveData = MutableLiveData<Result>()
    val saveLiveData: LiveData<Result> = _saveLiveData

    init {
        if (note != null)
            setUpImages(note.imageUrl)
    }

    @ExperimentalCoroutinesApi
    fun saveNote(note: Note) = viewModelScope.launch {
        note.imageUrl = getImagesUrl()
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

    fun updateNote(note: Note) = viewModelScope.launch {
        note.imageUrl = getImagesUrl()
        repository.updateNote(note)
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

    private fun getImagesUrl(): ArrayList<String>? {
        return if (images.isEmpty()) null else {
            val imageUrls = ArrayList<String>()
            images.forEach {
                imageUrls.add(it.toString())
            }
            imageUrls
        }

    }

    fun setUpImages(imageUrl: ArrayList<String>?) = viewModelScope.launch {
        imageUrl?.forEach {
            images.add(Uri.parse(it))
        }
    }
}