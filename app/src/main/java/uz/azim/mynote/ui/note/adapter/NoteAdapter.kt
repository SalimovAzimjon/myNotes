package uz.azim.mynote.ui.note.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.azim.mynote.R
import uz.azim.mynote.databinding.ItemNoteBinding
import uz.azim.mynote.entity.Note

class NoteAdapter : ListAdapter<Note, NoteViewHolder>(NotesDiffCallback()) {

    private var onClickListener: ((Note) -> Unit)? = null
    private var onLongClickListener: ((Note) -> Unit)? = null

    fun setOnNoteClickListener(listener: (Note) -> Unit) {
        onClickListener = listener
    }

    fun setOnLongClickListener(listener: (Note) -> Unit) {
        onLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = currentList[position]
        with(holder) {
            binding.noteTitle.text = note.title
            binding.noteBody.text = note.description
            binding.tvNoteDate.text = note.createdDate
            itemView.setOnClickListener { onClickListener?.invoke(note) }
            itemView.setOnLongClickListener {
                onLongClickListener?.invoke(note)
                return@setOnLongClickListener true
            }
        }
    }


}

class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemNoteBinding.bind(view)

}

class NotesDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem

}