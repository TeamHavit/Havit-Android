package org.sopt.havit.ui.sign

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddTosBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.ui.setting.SettingPersonalDataActivity
import org.sopt.havit.ui.setting.SettingPolicyActivity
import org.sopt.havit.util.MySharedPreference

@AndroidEntryPoint
class AddTosFragment : BaseBindingFragment<FragmentAddTosBinding>(R.layout.fragment_add_tos) {

    private val signInViewModel: SignViewModel by activityViewModels()

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
        binding.vm = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListeners()
        accessTokenObserver()
        initIsServerErrorObserver()
    }

    override fun onResume() {
        super.onResume()
        initOnBackPressed()
    }

    private fun setListeners() {
        binding.btnTosStart.setOnClickListener {
            signInViewModel.postSignUp()
        }
        binding.btnTosBack.setOnClickListener {
            signInViewModel.setMoveToNextOrBack(false)
        }
        binding.tvPolicy.setOnClickListener {
            startSettingPolicyActivity()
        }
        binding.tvPersonalData.setOnClickListener {
            startSettingPersonalDataActivity()
        }
        binding.layoutNetworkError.ivRefresh.setOnClickListener {
            signInViewModel.postSignUp()
        }
    }

    private fun initOnBackPressed() {
        requireActivity().onBackPressedDispatcher
            .addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        signInViewModel.setMoveToNextOrBack(false)
                    }
                }
            )
    }

    private fun startMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finishAffinity()
    }

    private fun startSettingPolicyActivity() {
        startActivity(Intent(requireContext(), SettingPolicyActivity::class.java))
    }

    private fun startSettingPersonalDataActivity() {
        startActivity(Intent(requireContext(), SettingPersonalDataActivity::class.java))
    }

    private fun initIsServerErrorObserver() {
        signInViewModel.isServerNetwork.observe(viewLifecycleOwner) {
            if (it == NetworkState.SUCCESS) {
                startMainActivity()
            }
        }
    }

    private fun accessTokenObserver() {
        signInViewModel.accessToken.observe(viewLifecycleOwner) {
            if (it != "") MySharedPreference.setXAuthToken(requireContext(), it)
        }
    }
}
