package org.sopt.havit.ui.share.edit_title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.ui.share.ShareViewModel
import org.sopt.havit.util.AutoClearedValue
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.KeyBoardUtil
import org.sopt.havit.util.OnBackPressedHandler

@AndroidEntryPoint
class EditTitleFragment : Fragment(), OnBackPressedHandler {
    private val viewModel: ShareViewModel by activityViewModels()
    private var binding: FragmentEditTitleBinding by AutoClearedValue()
    private val args by navArgs<EditTitleFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTitleBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOriginTitle()
        setKeyBoardUp()
        initClickListener()
        setCursor()
    }

    private fun setCursor() {
        binding.etTitle.post {
            binding.etTitle.requestFocus()
            binding.etTitle.setSelection(args.contentsOriginTitle.length)
        }
    }

    private fun setOriginTitle() = binding.etTitle.setText(
        viewModel.ogData.value?.ogTitle ?: throw IllegalStateException("Og Data Not Initialized")
    )

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(requireContext(), binding.etTitle)

    private val isTitleChanged: Boolean
        get() = args.contentsOriginTitle != binding.etTitle.text.toString()

    private fun initClickListener() {
        binding.icBack.setOnClickListener { onBackClicked() }
        binding.tvComplete.setOnClickListener {
            viewModel.ogData.value?.ogTitle = binding.etTitle.text.toString()
            goBack()
        }
    }

    private fun onBackClicked() {
        if (isTitleChanged) showEditTitleWarningDialog()
        else goBack()
    }

    override fun onBackPressed(): Boolean {
        if (isTitleChanged) {
            showEditTitleWarningDialog()
            return true
        }
        return false
    }

    private fun showEditTitleWarningDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_TITLE, ::goBack)
        dialog.show(requireActivity().supportFragmentManager, this.javaClass.name)
    }

    private fun goBack() {
        findNavController().popBackStack()
    }
}
