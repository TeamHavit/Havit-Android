package org.sopt.havit.ui.contents

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.havit.R
import org.sopt.havit.data.remote.CategoryResponse
import org.sopt.havit.databinding.FragmentDialogContentsCategoryBinding


class DialogContentsCategoryFragment(
    private val categoryList: ArrayList<CategoryResponse.AllCategoryData>,
    private val categoryName: String
) : DialogFragment() {
    private var _binding: FragmentDialogContentsCategoryBinding? = null
    val binding get() = _binding!!
    private var _contentsCategoryAdapter: ContentsCategoryAdapter? = null
    private val contentsCategoryAdapter get() = _contentsCategoryAdapter ?: error("adapter error")

    private val contentsCategoryViewModel: ContentsCategoryViewModel by lazy {
        ContentsCategoryViewModel(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dialog_contents_category,
            container,
            false
        )
        binding.contentsCategoryViewModel = contentsCategoryViewModel

        initAdapter()
        setData()
        decorationView()
        clickCategory()
        clickDialogClose()
        clickBack()
        initTopSheet()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setTopSheetAttribute() // 크기 지정은 onResume에서 해야함
    }

    private fun initAdapter() {
        _contentsCategoryAdapter = ContentsCategoryAdapter()
        binding.rvCategoryList.adapter = contentsCategoryAdapter
    }

    private fun setData() {
        binding.tvCategory.text = categoryName
        contentsCategoryAdapter.contentsCategoryList.addAll(categoryList)
        contentsCategoryAdapter.notifyDataSetChanged()
    }

    private fun decorationView() {
        binding.rvCategoryList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    // 클릭한 카테고리로 이동
    private fun clickCategory() {
        contentsCategoryAdapter.setItemCategoryClickListener(object :
            ContentsCategoryAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(requireActivity(), ContentsActivity::class.java)
                contentsCategoryAdapter.contentsCategoryList[position]
                    .let {
                        intent.putExtra("categoryId", it.id)
                        intent.putExtra("categoryName", it.title)
                    }
                startActivity(intent)
                requireActivity().finish()
            }
        })
    }

    // 다이얼로그 닫을 때
    private fun clickDialogClose(){
        binding.tvClose.setOnClickListener { this.dismiss() }
        binding.clCategory.setOnClickListener { this.dismiss() }
    }

    // 콘텐츠 뷰 뒤로가기 시
    private fun clickBack(){
        binding.ivBack.setOnClickListener { requireActivity().finish() }
    }

    // TopSheetDialog 초기화
    private fun initTopSheet() {
        val window = dialog!!.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 여백 제거
        window?.setGravity(Gravity.TOP) // 위치 조정
        val windowParams = window!!.attributes
        //windowParams.y = dpToPx(requireContext(), 59).toInt() // 상단바 크기만큼 y위치 지정
    }

    // 크기, 애니메이션 설정
    private fun setTopSheetAttribute() {
        // 크기 조정
        val params = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT

        // 리사이클러뷰에 대해 애니메이션 설정
        val fadeInAnim: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_from_top)
        binding.rvCategoryList.startAnimation(fadeInAnim)
        //params.windowAnimations = R.style.TopSheet_DialogAnimation // 다이얼로그 전체에 애니메이션 적용
        dialog!!.window!!.attributes = params
    }

    // dp를 px로 바꿔주는 함수
    private fun dpToPx(context: Context, dp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
    }
}