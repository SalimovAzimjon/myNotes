package uz.azim.mynote.ui.image

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import uz.azim.mynote.R
import uz.azim.mynote.databinding.FragmentImageBinding
import uz.azim.mynote.ui.BaseFragment
import uz.azim.mynote.ui.image.adapter.FullImageAdapter

class ImageFragment : BaseFragment<FragmentImageBinding>(R.layout.fragment_image) {

    private val args by navArgs<ImageFragmentArgs>()
    private val imageAdapter by lazy { FullImageAdapter(args.images.toCollection(ArrayList())) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()

    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = imageAdapter
    }

    override fun initViewBinding(view: View): FragmentImageBinding {
        return FragmentImageBinding.bind(view)
    }

    override fun subscribeObservers() {
    }

}