package uz.azim.mynote.ui.image.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.azim.mynote.R
import uz.azim.mynote.databinding.ItemFullImageBinding

class FullImageAdapter(private val images: ArrayList<Uri>) : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_full_image, parent, false)
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) {
        with(holder){
            binding.noteImage.load(images[position])
        }
    }
}

class PagerVH(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemFullImageBinding.bind(view)
}