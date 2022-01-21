package org.sopt.havit.ui.share

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.util.MySharedPreference


class EditTitleFragment : Fragment() {
    private var _binding: FragmentEditTitleBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EditTitleFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTitleBinding.inflate(layoutInflater, container, false)

        val tempTitle = args.contentsModifiedTitle
        binding.etTitle.setText(tempTitle)

        initClickListener()

        return binding.root
    }

    private fun initClickListener(){
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_editTitleFragment_to_contentsSummeryFragment)
        }
        binding.tvComplete.setOnClickListener {
            MySharedPreference.setTitle(requireContext(), binding.etTitle.text.toString())
            findNavController().navigate(R.id.action_editTitleFragment_to_contentsSummeryFragment)
//            findNavController().navigate(
//                EditTitleFragmentDirections.actionEditTitleFragmentToContentsSummeryFragment(
//                    binding.etTitle.text.toString()
//                )
//            )
        }
    }

}