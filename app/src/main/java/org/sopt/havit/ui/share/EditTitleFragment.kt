package org.sopt.havit.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.MySharedPreference


class EditTitleFragment : Fragment() {
    private var _binding: FragmentEditTitleBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<EditTitleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTitleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOriginTitle()
        setKeyBoardUp()
        initClickListener()

    }

    private fun setOriginTitle() = binding.etTitle.setText(args.contentsOriginTitle)

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(requireContext(), binding.etTitle)

    private fun initClickListener() {
        binding.icBack.setOnClickListener {
            val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_TITLE, ::goBack)
            dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
        }
        binding.tvComplete.setOnClickListener {
            MySharedPreference.setTitle(requireContext(), binding.etTitle.text.toString())
            findNavController().popBackStack()
        }
    }

    private fun goBack() {
        findNavController().popBackStack()
    }

}