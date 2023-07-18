package org.sopt.havit.ui.category

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_IMAGE_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_NAME
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_NAME_LIST
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_POSITION
import org.sopt.havit.ui.share.add_category.IconAdapter
import org.sopt.havit.ui.share.add_category.IconAdapter.Companion.clickedPosition
import org.sopt.havit.util.*

@AndroidEntryPoint
class CategoryContentModifyActivity :
    BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private val categoryViewModel by viewModels<CategoryViewModel>()
    private var position = -1
    private var id = -1
    private var originCategoryImageId = -1
    private lateinit var originCategoryName: String
    private lateinit var iconAdapter: IconAdapter
    private lateinit var categoryTitleList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initIconAdapter()
        setCategoryIntentData()
        checkCategoryTitleValidation()
        setBackBtnClickListener()
        setDeleteTitleBtnClickListener()
        setDeleteBtnClickListener()
        setModifyCompleteBtnClickListener()
        observeDeleteState()
        observeModifyState()
    }

    override fun onBackPressed() {
        setBackPressedAction()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setCategoryIntentData() {
        binding.categoryTitle =
            intent.getStringExtra(CATEGORY_NAME).toString().also {
                originCategoryName = it
            }
        position = intent.getIntExtra(CATEGORY_POSITION, 0)
        id = intent.getIntExtra(CATEGORY_ID, 0)
        categoryTitleList = intent.getStringArrayListExtra(CATEGORY_NAME_LIST) as ArrayList<String>
    }

    private fun initIconAdapter() {
        clickedPosition = (intent.getIntExtra(CATEGORY_IMAGE_ID, 0) - 1).also {
            originCategoryImageId = it
        }
        binding.rvIcon.adapter = IconAdapter(::onIconClick).also { iconAdapter = it }
    }

    private fun onIconClick(position: Int) {
        val previousPosition = clickedPosition
        clickedPosition = position
        iconAdapter.notifyItemChanged(previousPosition)
        iconAdapter.notifyItemChanged(clickedPosition)
    }

    private fun setBackBtnClickListener() {
        binding.ivBack.setOnSingleClickListener { setBackPressedAction() }
    }

    private fun setDeleteTitleBtnClickListener() {
        binding.ivDeleteText.setOnSingleClickListener { deleteCategoryTitle() }
    }

    private fun checkCategoryTitleValidation() {
        binding.etCategory.addTextChangedListener {
            binding.categoryTitle?.let { categoryTitle ->
                binding.isOnlySpace = isOnlySpace(categoryTitle)
                binding.isDuplicated = isDuplicated(categoryTitle)
            }
        }
    }

    private fun isOnlySpace(categoryTitle: String): Boolean =
        categoryTitle.isNotEmpty() && categoryTitle.trim().isEmpty()

    private fun isDuplicated(categoryTitle: String): Boolean =
        (categoryTitle.trim() in categoryTitleList && categoryTitle.trim() != originCategoryName)

    private fun deleteCategoryTitle() = binding.etCategory.text.clear()

    private fun setBackPressedAction() {
        if (isNotModified()) {
            finish()
        } else {
            showBackAlertDialog()
        }
    }

    private fun isNotModified(): Boolean {
        binding.categoryTitle?.let { categoryTitle ->
            return (categoryTitle.trim() == originCategoryName && clickedPosition == originCategoryImageId)
        }
        return false
    }

    // 뒤로가기 시 뜨는 dialog
    private fun showBackAlertDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_CATEGORY, ::finish)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    // 삭제 버튼 클릭 시
    private fun showCategoryDeleteDialog() {
        val dialog = DialogUtil(DialogUtil.REMOVE_CATEGORY, ::requestCategoryDelete)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun setDeleteBtnClickListener() {
        binding.btnRemove.setOnSingleClickListener { showCategoryDeleteDialog() }
    }

    // 완료 버튼 클릭 시
    private fun setModifyCompleteBtnClickListener() {
        binding.tvComplete.setOnSingleClickListener {
            requestCategoryModify()
        }
    }

    private fun requestCategoryModify() {
        // 서버로 카테고리 내용 수정 요청
        categoryViewModel.requestCategoryContent(
            id = id,
            imageId = clickedPosition + 1,
            title = binding.categoryTitle!!.trim()
        )
    }

    private fun requestCategoryDelete() {
        // 서버에 해당 카테고리 삭제 요청
        categoryViewModel.requestCategoryDelete(id)
    }

    private fun sendCategoryModifyResult() {
        callingActivity?.let { callingActivity ->
            val callingActivityName = callingActivity.className

            val intent = Intent(this, callingActivity.className.javaClass).apply {
                putExtra(CATEGORY_NAME, binding.categoryTitle!!.trim())
                putExtra(CATEGORY_IMAGE_ID, clickedPosition + 1)
            }
            if (callingActivityName == CategoryOrderModifyActivity::class.java.name) {
                intent.putExtra(CATEGORY_POSITION, position)
            }
            setResult(RESULT_MODIFY_CATEGORY, intent)
        }
    }

    private fun sendCategoryDeleteResult() {
        callingActivity?.let { callingActivity ->
            val callingActivityName = callingActivity.className

            val intent = Intent(this, callingActivity.className.javaClass)
            if (callingActivityName == CategoryOrderModifyActivity::class.java.name) {
                intent.putExtra(CATEGORY_POSITION, position)
            }
            setResult(RESULT_DELETE_CATEGORY, intent)
        }
    }

    private fun observeDeleteState() {
        categoryViewModel.deleteState.observe(this) {
            when (it) {
                NetworkState.FAIL -> ToastUtil(this@CategoryContentModifyActivity).makeToast(
                    ERROR_OCCUR_TYPE
                )
                NetworkState.SUCCESS -> {
                    ToastUtil(this@CategoryContentModifyActivity).makeToast(
                        DELETE_CATEGORY_TOP_TYPE
                    )

                    sendCategoryDeleteResult()
                    finish()
                }
                else -> {}
            }
        }
    }

    private fun observeModifyState() {
        categoryViewModel.modifyState.observe(this) {
            when (it) {
                NetworkState.FAIL -> ToastUtil(this@CategoryContentModifyActivity).makeToast(
                    ERROR_OCCUR_TYPE
                )
                NetworkState.SUCCESS -> {
                    sendCategoryModifyResult()
                    finish()
                    ToastUtil(this@CategoryContentModifyActivity).makeToast(
                        CATEGORY_MODIFY_COMPLETE_TYPE
                    )
                }
                else -> {}
            }
        }
    }

    companion object {
        const val RESULT_DELETE_CATEGORY = 1000
        const val RESULT_MODIFY_CATEGORY = 2000
    }
}
