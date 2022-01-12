package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentNoCategoryBinding

class NoCategoryFragment : Fragment() {

    private var _binding: FragmentNoCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoCategoryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }
}