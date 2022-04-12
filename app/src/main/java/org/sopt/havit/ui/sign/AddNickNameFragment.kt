package org.sopt.havit.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddNickNameBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class AddNickNameFragment :
    BaseBindingFragment<FragmentAddNickNameBinding>(R.layout.fragment_add_nick_name) {

    private val signInViewModel: SignInViewModel by activityViewModels()
    private val args by navArgs<AddNickNameFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.vm = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNickName()
        setTextWatcher()
        setListener()
    }

    private fun setNickName() {
        binding.etNickname.setText(args.nickName)
        signInViewModel.nickName.value = args.nickName
    }

    private fun setTextWatcher() {
        binding.etNickname.addTextChangedListener {
            signInViewModel.nickName.value = binding.etNickname.text.toString()
        }
    }

    private fun setListener() {
        binding.btnNicknameNext.setOnClickListener {
            findNavController().navigate(R.id.action_addNickNameFragment_to_addTosFragment)
        }
        binding.ivNicknameDeleteText.setOnClickListener {
            binding.etNickname.setText("")
        }
        binding.btnNicknameBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}