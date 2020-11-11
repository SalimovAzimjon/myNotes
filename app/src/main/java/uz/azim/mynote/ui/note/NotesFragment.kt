package uz.azim.mynote.ui.note

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import uz.azim.mynote.MY_PREF
import uz.azim.mynote.R
import uz.azim.mynote.TARGET_VIEW
import uz.azim.mynote.databinding.FragmentNotesBinding
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.ui.BaseFragment
import uz.azim.mynote.ui.note.adapter.NoteAdapter
import uz.azim.mynote.util.Resource
import uz.azim.mynote.util.ViewModelFactory
import uz.azim.mynote.util.showSnackbar

class NotesFragment : BaseFragment<FragmentNotesBinding>(R.layout.fragment_notes) {

    private lateinit var preferences: SharedPreferences
    private val noteAdapter by lazy(LazyThreadSafetyMode.NONE) { NoteAdapter() }
    private val noteVM by viewModels<NoteViewModel> { ViewModelFactory(NoteRepository()) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNotes()
        setTargetView()
        setUpListeners()
    }

    private fun getNotes() {
        noteVM.getNotes()
    }

    override fun initViewBinding(view: View): FragmentNotesBinding {
        return FragmentNotesBinding.bind(view)
    }

    override fun subscribeObservers() {
        noteVM.note.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    //TODO add loading
                }
                is Resource.Error -> {
                    showSnackbar(binding.rvNotes, it.message.toString())
                }
                is Resource.Success -> {
                    it.data?.run {
                        binding.tvNothing.isVisible = this.isEmpty()
                        noteAdapter.submitList(this)
                        setUpRv()
                    }
                }
            }
        })
    }

    private fun setUpRv() {
        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setTargetView() {
        preferences = requireActivity().getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        val isTargetViewShown = preferences.getBoolean(TARGET_VIEW, false)
        if (!isTargetViewShown)
            showTargetView()
    }

    private fun showTargetView() {
        TapTargetView.showFor(
            requireActivity(),
            TapTarget.forView(
                binding.addNote,
                "Add notes",
                "This buttons is for adding notes"
            ).tintTarget(false),
            object : TapTargetView.Listener() {
                override fun onTargetDismissed(view: TapTargetView?, userInitiated: Boolean) {
                    super.onTargetDismissed(view, userInitiated)
                    val editor = preferences.edit()
                    editor.putBoolean(TARGET_VIEW, true)
                    editor.apply()
                }

                override fun onOuterCircleClick(view: TapTargetView?) {
                    super.onOuterCircleClick(view)
                    view?.dismiss(true)
                }
            }
        )
    }

    private fun setUpListeners() {
        binding.addNote.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.notesFragment)
                findNavController().navigate(R.id.action_notesFragment_to_editNoteFragment)
        }

        noteAdapter.setOnNoteClickListener {
            if (findNavController().currentDestination?.id == R.id.notesFragment) {
                val action = NotesFragmentDirections.actionNotesFragmentToEditNoteFragment(it)
                findNavController().navigate(action)
            }
        }
    }
}