package org.sopt.havit.ui.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentAddNickNameBinding
import org.sopt.havit.ui.base.BaseBindingFragment

class AddNickNameFragment :
    BaseBindingFragment<FragmentAddNickNameBinding>(R.layout.fragment_add_nick_name) {

    private val args by navArgs<AddNickNameFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNickName()
    }

    private fun setNickName() {
        binding.etNickname.setText(args.nickName)
    }


}