package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentContentsSummeryBinding

class ContentsSummeryFragment : Fragment() {
    private var _binding :FragmentContentsSummeryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentsSummeryBinding.inflate(layoutInflater, container, false)

        // TODO : imageview 모서리 둥글게
        binding.ivOgImage.clipToOutline = true

        return binding.root
    }
}