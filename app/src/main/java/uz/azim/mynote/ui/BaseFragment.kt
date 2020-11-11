package uz.azim.mynote.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(@LayoutRes layout: Int) : Fragment(layout) {
    protected lateinit var binding: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = initViewBinding(view)
        subscribeObservers()
    }

    abstract fun initViewBinding(view: View):T

    abstract fun subscribeObservers()
}