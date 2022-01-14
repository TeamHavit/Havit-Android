package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentChooseIconBinding
import org.sopt.havit.databinding.FragmentNoCategoryBinding

class ChooseIconFragment : Fragment() {
    private var _binding: FragmentChooseIconBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseIconBinding.inflate(layoutInflater, container, false)

        binding.btnNext.setOnClickListener {

            // TODO: snack bar로 custom (release)
            val toast = Toast(requireContext())
            // set text
            val view = layoutInflater.inflate(R.layout.toast_category_added, null)
            val textView : TextView = view.findViewById(R.id.tv_toast_category1)
            textView.text = "test"
            toast.view = view
            toast.show()

        }

        return binding.root
    }

}