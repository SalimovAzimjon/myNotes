package uz.azim.mynote.ui.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.azim.mynote.R
import uz.azim.mynote.databinding.FragmentEditBinding
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.ui.BaseFragment
import uz.azim.mynote.util.Result
import uz.azim.mynote.util.ViewModelFactory
import uz.azim.mynote.util.hideKeyboard
import uz.azim.mynote.util.showSnackbar
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
class EditNoteFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {

    private val args by navArgs<EditNoteFragmentArgs>()
    private val editVM by viewModels<EditFragmentVM> { ViewModelFactory(NoteRepository()) }
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private var isEditMode = false
    private var note: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.note?.run {
            note = this
            isEditMode = true
            binding.titleInput.setText(this.title)
            binding.noteInput.setText(this.description)
            //TODO handle images
        }
        setUpListeners()
    }

    private fun setUpListeners() {
        binding.saveNote.setOnClickListener {
            if (binding.titleInput.text.toString().isEmpty()) {
                showSnackbar(binding.inputLayout, "Title can't be empty")
            } else {
                modifyNote()
            }
        }

        binding.btnBack.setOnClickListener {
            binding.saveNote.hideKeyboard()
            findNavController().popBackStack()
        }
    }

    private fun modifyNote() {
        if (isEditMode) {

        } else {
            val date = dateFormat.format(Calendar.getInstance().time)
            val note = Note(
                binding.titleInput.text.toString(),
                binding.noteInput.text.toString(),
                date,
                false
            )
            editVM.saveNote(note)
        }
    }

    override fun initViewBinding(view: View): FragmentEditBinding {
        return FragmentEditBinding.bind(view)
    }

    override fun subscribeObservers() {
        editVM.saveLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it!!) {
                Result.LOADING -> {
                    isLoading(true)
                }
                Result.ERROR -> {
                    isLoading(false)
                }
                Result.SUCCESS -> {
                    isLoading(true)
                    showSnackbar(binding.noteInput, "Note saved")
                    binding.saveNote.hideKeyboard()
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun isLoading(isLoading: Boolean) {
        binding.saveNote.isEnabled = !isLoading
        binding.progress.isVisible = isLoading
    }

}