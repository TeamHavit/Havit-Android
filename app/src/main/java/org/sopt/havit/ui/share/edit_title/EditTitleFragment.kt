package org.sopt.havit.ui.share.edit_title

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.databinding.FragmentEditTitleBinding
import org.sopt.havit.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditTitleFragment : Fragment(), OnBackPressedHandler {

    @Inject
    lateinit var preference: HavitSharedPreference

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

    private fun setOriginTitle() = binding.etTitle.setText(args.contentsOriginTitle)

    private fun setKeyBoardUp() = KeyBoardUtil.openKeyBoard(requireContext(), binding.etTitle)

    private val isTitleChanged: Boolean
        get() = args.contentsOriginTitle != binding.etTitle.text.toString()

    private fun initClickListener() {
        binding.icBack.setOnClickListener { onBackClicked() }
        binding.tvComplete.setOnClickListener {
            preference.setTitle(binding.etTitle.text.toString())
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
