package org.sopt.havit.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseBindingFragment<B : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    Fragment() {
    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initBinding(inflater, container)
        return binding.root
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = DataBindingUtil.inflate<B>(inflater, layoutResId, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}