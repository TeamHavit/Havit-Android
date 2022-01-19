package org.sopt.havit.ui.contents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentContentsMoreBinding


class ContentsMoreFragment : BottomSheetDialogFragment() {

    private lateinit var binding :FragmentContentsMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentContentsMoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}