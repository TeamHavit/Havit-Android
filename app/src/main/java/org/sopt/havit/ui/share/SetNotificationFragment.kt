package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.sopt.havit.databinding.FragmentSetNotificationBinding

class SetNotificationFragment : Fragment() {
    private var _binding: FragmentSetNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetNotificationBinding.inflate(layoutInflater, container, false)

        binding.btn1h.setOnClickListener {
            binding.btn1h?.isSelected = binding.btn1h?.isSelected != true
        }
        binding.btn2h.setOnClickListener {
            binding.btn1h?.isSelected = binding.btn1h?.isSelected != true
        }
        binding.btn3h.setOnClickListener {
            binding.btn1h?.isSelected = binding.btn1h?.isSelected != true
        }
        binding.btnTomorrow.setOnClickListener {
            binding.btn1h?.isSelected = binding.btn1h?.isSelected != true
        }

        return binding.root
    }

}