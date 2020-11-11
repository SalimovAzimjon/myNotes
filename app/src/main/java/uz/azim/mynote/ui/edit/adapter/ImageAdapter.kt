package uz.azim.mynote.ui.edit.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.azim.mynote.R
import uz.azim.mynote.databinding.ItemImgBinding

class ImageAdapter : ListAdapter<Uri, ImageVH>(ImageDiffCallback()) {
    private var onClickListener: ((Uri) -> Unit)? = null

    fun setOnDeleteClickListener(listener: (Uri) -> Unit) {
        onClickListener = listener
    }

    private var onImageClickListener: (() -> Unit)? = null

    fun setOnImageClickListener(listener: (() -> Unit)) {
        onImageClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img, parent, false)
        return ImageVH(view)
    }

    override fun onBindViewHolder(holder: ImageVH, position: Int) {
        val img = currentList[position]
        with(holder) {
            binding.imgNote.load(img)
            binding.btnDelete.setOnClickListener {
                onClickListener?.invoke(img)
            }
            binding.imgNote.setOnClickListener {
                onImageClickListener?.invoke()
            }
        }
    }

}

class ImageVH(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemImgBinding.bind(view)
}

class ImageDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem.path == newItem.path

}