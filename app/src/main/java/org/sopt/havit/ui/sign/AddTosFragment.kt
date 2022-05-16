package org.sopt.havit.ui.sign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddTosBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class AddTosFragment : BaseBindingFragment<FragmentAddTosBinding>(R.layout.fragment_add_tos) {

    private val signInViewModel: SignInViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListeners()
        accessTokenObserver()
    }

    private fun setListeners() {
        binding.btnTosStart.setOnClickListener {
            signInViewModel.postSignUp()
        }
        binding.btnTosBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.ivTosAll.setOnClickListener {
            signInViewModel.setAllCheck()
        }
        binding.ivTosUse.setOnClickListener {
            signInViewModel.setTosUseCheck()
        }
        binding.ivTosInfo.setOnClickListener {
            signInViewModel.setTosInfoCheck()
        }
        binding.ivTosEvent.setOnClickListener {
            signInViewModel.setTosEventCheck()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun accessTokenObserver() {
        signInViewModel.accessToken.observe(viewLifecycleOwner) {
            startMainActivity()
            signInViewModel.saveAccessToken()
            MySharedPreference.setXAuthToken(requireContext(), it ?: "")
        }
    }
}