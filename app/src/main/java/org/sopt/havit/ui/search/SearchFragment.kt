package org.sopt.havit.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import org.koin.android.viewmodel.ext.android.viewModel
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentSearchBinding
import org.sopt.havit.ui.base.BaseBindingFragment


class SearchFragment : BaseBindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel:SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setListeners()
        return binding.root
    }

    private fun setListeners() = binding.etSearch.addTextChangedListener(
        object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }

        }

    )


}