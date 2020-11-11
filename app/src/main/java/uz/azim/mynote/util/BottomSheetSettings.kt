package uz.azim.mynote.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.azim.mynote.R
import uz.azim.mynote.databinding.BottomSheetSettingsBinding
import uz.azim.mynote.entity.Note

class BottomSheetSettings(
    private val noteTitle: String,
    private var onDoneClick: (() -> Unit),
    private var onDeleteClick: (() -> Unit)
) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = BottomSheetSettingsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = noteTitle
        binding.btnDelete.setOnClickListener {
            onDeleteClick.invoke()
            dismiss()
        }
        binding.btnMarkDone.setOnClickListener {
            onDoneClick.invoke()
            dismiss()
        }
    }
}