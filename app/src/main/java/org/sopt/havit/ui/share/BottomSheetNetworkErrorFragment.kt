package org.sopt.havit.ui.share

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentBottomSheetNetworkErrorBinding
import org.sopt.havit.ui.share.ShareActivity.Companion.ON_NETWORK_ERROR_DISMISS
import org.sopt.havit.util.setOnSingleClickListener

class BottomSheetNetworkErrorFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetNetworkErrorBinding? = null
    private val binding get() = _binding!!
    private lateinit var onDismiss: () -> Unit
    private val viewModel: ShareViewModel by activityViewModels()

    // Round Corner
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onDismiss = it.getSerializable(ON_NETWORK_ERROR_DISMISS) as () -> Unit
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetNetworkErrorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetHeight()
        onClickCloseButton()
        onClickRefreshButton()
    }

    private fun onClickCloseButton() {
        binding.ibClose.setOnClickListener {
            dismiss()
            requireActivity().finish()
        }
    }

    private fun setBottomSheetHeight() {
        (dialog as BottomSheetDialog).behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED // 높이 고정
            skipCollapsed = true // HALF_EXPANDED 안되게 설정
        }
        binding.bottomSheetNetworkError.layoutParams.height =
            (resources.displayMetrics.heightPixels * 0.94).toInt()
    }

    private fun onClickRefreshButton() {
        binding.ivRefresh.setOnSingleClickListener {
            it.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(),
                    R.anim.rotation_refresh
                )
            )
            viewModel.makeSignIn(internetError = {},
                onUnAuthorized = { dismiss() },
                onAuthorized = { dismiss() })
        }
    }

    override fun onDestroy() {
        onDismiss()
        super.onDestroy()
        _binding = null
    }

}