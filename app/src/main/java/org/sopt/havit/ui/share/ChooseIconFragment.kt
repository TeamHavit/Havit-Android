package org.sopt.havit.ui.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.data.remote.CategoryAddRequest
import org.sopt.havit.databinding.FragmentChooseIconBinding
import org.sopt.havit.ui.base.BaseBindingFragment
import org.sopt.havit.util.MySharedPreference

class ChooseIconFragment :
    BaseBindingFragment<FragmentChooseIconBinding>(R.layout.fragment_choose_icon) {
    private lateinit var iconAdapter: IconAdapter
    private val args by navArgs<ChooseIconFragmentArgs>()
    private var categoryIndex = 0   // default 로 첫번째 아이콘 선택

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initClickNext()
        toolbarClickListener()

    }

    private fun toolbarClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_chooseIconFragment_to_addCategoryFragment)
        }
        binding.icClose.setOnClickListener { requireActivity().finish() }
    }

    private fun initAdapter() {

        // init data
        iconAdapter = IconAdapter()
        binding.rvIcon.adapter = iconAdapter
        iconAdapter.iconList.addAll(iconDrawableList)
        iconAdapter.notifyItemRangeInserted(0, iconDrawableList.size)

        // set clickListener
        iconAdapter.setItemClickListener(object : IconAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                categoryIndex = position
            }
        })
    }


    private fun initClickNext() {
        binding.btnNext.setOnClickListener {
            initNetwork()
            findNavController().navigate(R.id.action_chooseIconFragment_to_selectCategoryFragment)
        }
    }

    private fun initNetwork() {
        lifecycleScope.launch {
            kotlin.runCatching {
                val response =
                    RetrofitObject.provideHavitApi(MySharedPreference.getXAuthToken(requireContext()))
                        .addCategory(CategoryAddRequest(args.categoryTitle, categoryIndex + 1))
                showCustomToast()
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

    companion object {
        val iconDrawableList = listOf(
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
    }
}