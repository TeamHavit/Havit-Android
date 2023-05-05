package org.sopt.havit.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddNickNameBinding
import org.sopt.havit.ui.base.BaseBindingFragment

@AndroidEntryPoint
class AddNickNameFragment :
    BaseBindingFragment<FragmentAddNickNameBinding>(R.layout.fragment_add_nick_name) {

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setTextWatcher()
        setListener()
    }

    override fun onResume() {
        super.onResume()
        initOnBackPressed()
    }

    private fun setTextWatcher() {
        binding.etNickname.addTextChangedListener {
            val hasSpace = binding.etNickname.text?.toString()?.contains("\\s".toRegex()) ?: false
            if (hasSpace) { // 닉네임에 띄어쓰기 포함
                signUpViewModel.nickNameStatus.value = NickNameTextStatus.HAS_SPACING
            } else if (binding.etNickname.text?.toString()?.isEmpty() == true) { // 닉네임이 공백일 때
                signUpViewModel.nickNameStatus.value = NickNameTextStatus.EMPTY
            } else { // 정상 닉네임 입력할 때
                signUpViewModel.nickNameStatus.value = NickNameTextStatus.FILLED
            }
            binding.etNickname.setSelection(binding.etNickname.length())
        }
    }

    private fun setListener() {
        binding.btnNicknameNext.setOnClickListener {
            signUpViewModel.setNickName(binding.etNickname.text.toString())
            signUpViewModel.setMoveToNextOrBack(true)
        }
        binding.ivNicknameDeleteText.setOnClickListener {
            signUpViewModel.setClearNickName()
        }
        binding.btnNicknameBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        requireActivity().finish()
                    }
                }
            )
    }
}
