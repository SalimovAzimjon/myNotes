package uz.azim.mynote.ui.edit

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.azim.mynote.MY_PREF
import uz.azim.mynote.R
import uz.azim.mynote.TARGET_VIEW_EDIT
import uz.azim.mynote.databinding.FragmentEditBinding
import uz.azim.mynote.entity.Note
import uz.azim.mynote.repository.NoteRepository
import uz.azim.mynote.ui.BaseFragment
import uz.azim.mynote.ui.edit.adapter.ImageAdapter
import uz.azim.mynote.util.Result
import uz.azim.mynote.util.ViewModelFactory
import uz.azim.mynote.util.hideKeyboard
import uz.azim.mynote.util.showSnackbar
import java.text.SimpleDateFormat
import java.util.*


private val PERMISSION_CODE: Int = 1001
private val PICK_IMAGE: Int = 1002

@ExperimentalCoroutinesApi
class EditNoteFragment : BaseFragment<FragmentEditBinding>(R.layout.fragment_edit) {

    private lateinit var preferences: SharedPreferences
    private val args by navArgs<EditNoteFragmentArgs>()
    private val editVM by viewModels<EditFragmentVM> { ViewModelFactory(NoteRepository(),args.note) }
    private val imgAdapter by lazy { ImageAdapter() }
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
        }
        setTargetView()
        setUpRv()
        setUpListeners()
    }

    private fun setTargetView() {
        preferences = requireActivity().getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        val isTargetViewShown = preferences.getBoolean(TARGET_VIEW_EDIT, false)
        if (!isTargetViewShown)
            showTargetView()
    }

    private fun setUpRv() {
        imgAdapter.submitList(editVM.images.toMutableList())
        binding.rvImg.apply {
            adapter = imgAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun showTargetView() {
        TapTargetSequence(requireActivity())
            .targets(
                TapTarget.forView(
                    binding.saveNote,
                    "Save notes",
                    "This buttons is for saving notes"
                ).tintTarget(false),
                TapTarget.forView(
                    binding.btnSetImage,
                    "Add images",
                    "You can add images by pressing this button"
                ).tintTarget(false)
            ).listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val editor = preferences.edit()
                    editor.putBoolean(TARGET_VIEW_EDIT, true)
                    editor.apply()
                }

                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}

                override fun onSequenceCanceled(lastTarget: TapTarget?) {}

            }).start()
    }

    private fun setUpListeners() {
        binding.saveNote.setOnClickListener {
            if (binding.titleInput.text.toString().isEmpty()) {
                binding.titleInputLayout.error = getString(R.string.title_empty)
            } else {
                modifyNote()
            }
        }

        binding.btnBack.setOnClickListener {
            binding.saveNote.hideKeyboard()
            findNavController().popBackStack()
        }

        binding.btnSetImage.setOnClickListener {
            pickImage()
        }

        imgAdapter.setOnDeleteClickListener { img ->
            AlertDialog.Builder(requireContext())
                .setMessage("Do you want to delete this image")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, _ ->
                    val pos = editVM.images.indexOf(img)
                    editVM.images.remove(img)
                    imgAdapter.submitList(editVM.images.toMutableList())
                }
                .create().show()
        }

        imgAdapter.setOnImageClickListener {
            val action =
                EditNoteFragmentDirections.actionEditNoteFragmentToImageFragment(editVM.images.toTypedArray())
            if (findNavController().currentDestination?.id == R.id.editNoteFragment)
                findNavController().navigate(action)
        }
    }

    private fun pickImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) openGallery()
        else
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE)
    }

    private fun openGallery() {
        val gallery: Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            gallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
            gallery.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        } else {
            gallery = Intent(Intent.ACTION_GET_CONTENT)
        }
        gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        gallery.type = "image/*"
        startActivityForResult(
            Intent.createChooser(
                gallery,
                resources.getString(R.string.form_pick_photos)
            ), PICK_IMAGE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            data?.data?.let {
                val takeFlags = data.flags and Intent.FLAG_GRANT_READ_URI_PERMISSION
                val resolver = requireActivity().contentResolver
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    resolver.takePersistableUriPermission(it, takeFlags)
                }
                if (editVM.images.contains(it))
                    showSnackbar(binding.noteInput, "Photo exists")
                else {
                    editVM.images.add(it)
                    imgAdapter.submitList(editVM.images.toMutableList())
                }
            }
        }
    }

    private fun modifyNote() {
        if (isEditMode) {
            val date = dateFormat.format(Calendar.getInstance().time)
            note?.apply {
                title = binding.titleInput.text.toString()
                description = binding.noteInput.text.toString()
                updateDate = date
            }
            editVM.updateNote(note!!)
        } else {
            val date = dateFormat.format(Calendar.getInstance().time)
            note = Note(
                binding.titleInput.text.toString(),
                binding.noteInput.text.toString(),
                date,
                false
            )
            editVM.saveNote(note!!)
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
        binding.saveNote.isVisible = !isLoading
        binding.progress.isVisible = isLoading
    }


}