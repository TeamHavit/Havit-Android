package org.sopt.havit.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.databinding.FragmentCategoryIconBinding
import org.sopt.havit.ui.share.IconAdapter
import org.sopt.havit.util.MySharedPreference

class CategoryIconFragment : Fragment() {
    private lateinit var iconAdapter: IconAdapter
    private var _binding: FragmentCategoryIconBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CategoryIconFragmentArgs>()
    private var categoryIndex = -1
    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryIconBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initClickNext()
        toolbarClickListener()
        Log.d("ChooseIconFragment", "${args.categoryTitle}")

    }

    private fun toolbarClickListener(){
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.icClose.setOnClickListener {
            requireActivity().finish()
        }
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
                categoryIndex = position + 1
                checkIsSelected()
            }
        })
    }

    private fun checkIsSelected() {
        if (categoryIndex != -1) {
            Log.d("categoryButtonTest", "ok")
            binding.btnNext.setBackgroundResource(R.drawable.rectangle_purple)
            binding.btnNext.isEnabled = true
        }
    }

    private fun initClickNext() {
        binding.btnNext.setOnClickListener {

            initNetwork()
            showCustomToast()

        }
    }

    private fun initNetwork() {
//        lifecycleScope.launch {
//            try {
//                // 서버 통신
//                val response =
//                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
//                        .addCategory(CategoryAddRequest(args.categoryTitle, categoryIndex))
//                Log.d("CreateCategory", response.success.toString())
//            } catch (e: Exception) {
//                // 서버 통신 실패 시
//                Log.d("CreateCategory", e.toString())
//            }
//        }
        categoryViewModel.requestAddCategory(args.categoryTitle, categoryIndex)
        categoryViewModel.addDelay.observe(viewLifecycleOwner){
            if(it == true){
                Log.d("CreateCategory", "delay")
                categoryViewModel.setAddDelay(false)
                requireActivity().finish()
            }
        }
    }

    private fun showCustomToast() {
        // TODO: snack bar로 custom (release)
        val toast = Toast(requireContext())
        // set text
        val view = layoutInflater.inflate(R.layout.toast_category_added, null)
        val textView: TextView = view.findViewById(R.id.tv_toast_category1)
        textView.text = args.categoryTitle
        toast.view = view
        toast.show()
    }

}