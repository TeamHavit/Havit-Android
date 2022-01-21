package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        initListener()
        toolbarClickListener()

        return binding.root
    }

    private fun initListener() {
        binding.btnComplete.setOnClickListener {
            setCustomToast()
        }
        binding.ibEditTitle.setOnClickListener{
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_editTitleFragment)
        }
        binding.tvSetAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_setNotificationFragment)
        }
    }

    private fun toolbarClickListener(){
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_contentsSummeryFragment_to_selectCategoryFragment)
        }

        binding.icClose.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setCustomToast() {
        // TODO: snack bar 로 custom (release)
        val toast = Toast(requireContext())
        toast.setGravity(Gravity.TOP, 0, 54)
        // set text
        val view = layoutInflater.inflate(R.layout.toast_contents_added, null)
        toast.view = view
        toast.show()
    }
}