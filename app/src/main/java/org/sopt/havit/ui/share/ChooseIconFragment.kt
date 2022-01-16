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
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentChooseIconBinding

class ChooseIconFragment : Fragment() {
    private lateinit var iconAdapter : IconAdapter
    private var _binding: FragmentChooseIconBinding? = null
    private val binding get() = _binding!!

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

    }

    private fun initAdapter() {

        iconAdapter = IconAdapter()
        binding.rvIcon.adapter = iconAdapter
        iconAdapter.iconList.addAll(
            listOf(
                dummyImg,dummyImg2,dummyImg,dummyImg2,dummyImg,dummyImg2,dummyImg,dummyImg2,dummyImg,dummyImg2,
                dummyImg,dummyImg2,dummyImg,dummyImg2,dummyImg
            )
        )
        iconAdapter.notifyDataSetChanged()


        iconAdapter.setItemClickListener(object : IconAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("IconAdapter", "$position clicked in Fragment")
                v.background = ContextCompat.getDrawable(requireContext(), R.drawable.oval_gray_stroke_2)
            }
        } )
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


    companion object {
        const val dummyImg =
            "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png"
        const val dummyImg2 =
            "https://user-images.githubusercontent.com/59546818/149669476-3387dddf-d3cb-464d-a1a7-985cc3f397aa.png"
    }

}