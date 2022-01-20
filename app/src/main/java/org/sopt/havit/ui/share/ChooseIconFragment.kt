package org.sopt.havit.ui.share

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentChooseIconBinding

class ChooseIconFragment : Fragment() {
    private lateinit var iconAdapter: IconAdapter
    private var _binding: FragmentChooseIconBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ChooseIconFragmentArgs>()
    private var categoryIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseIconBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initClickNext()
        Log.d("ChooseIconFragment", "${args.categoryTitle}")

    }

    private fun initAdapter() {

        iconAdapter = IconAdapter()
        binding.rvIcon.adapter = iconAdapter
        iconAdapter.iconList.addAll(
            listOf(
                R.drawable.ic_category1,
                R.drawable.ic_category2,
                R.drawable.ic_category3,
                R.drawable.ic_category4,
                R.drawable.ic_category5,
                R.drawable.ic_category6,
                R.drawable.ic_category7,
                R.drawable.ic_category8,
                R.drawable.ic_category9,
                R.drawable.ic_category10,
                R.drawable.ic_category11,
                R.drawable.ic_category12,
                R.drawable.ic_category13,
                R.drawable.ic_category14,
                R.drawable.ic_category15
            )
        )
        iconAdapter.notifyDataSetChanged()


        iconAdapter.setItemClickListener(object : IconAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.d("IconAdapter", "$position clicked in Fragment")
                v.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_stroke_2)
                categoryIndex = position
            }
        })
    }

    private fun initClickNext() {
        binding.btnNext.setOnClickListener {

            findNavController().navigate(R.id.action_chooseIconFragment_to_selectCategoryFragment)
            showCustomToast()

        }
    }

    private fun showCustomToast() {
        // TODO: snack bar로 custom (release)
        val toast = Toast(requireContext())
        // set text
        val view = layoutInflater.inflate(R.layout.toast_category_added, null)
        val textView: TextView = view.findViewById(R.id.tv_toast_category1)
        textView.text = "test"
        toast.view = view
        toast.show()
    }

}