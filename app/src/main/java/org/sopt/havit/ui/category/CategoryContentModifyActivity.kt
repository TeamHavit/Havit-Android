package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_IMAGE_ID
import org.sopt.havit.ui.category.CategoryFragment.Companion.CATEGORY_NAME
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.ui.share.add_category.IconAdapter
import org.sopt.havit.ui.share.add_category.IconAdapter.Companion.clickedPosition
import org.sopt.havit.util.*

@AndroidEntryPoint
class CategoryContentModifyActivity :
    BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(this) }
    private var position = -1
    private var id = -1
    private var originCategoryImageId = -1
    private lateinit var originCategoryName: String
    private lateinit var iconAdapter: IconAdapter
    private lateinit var categoryTitleList: ArrayList<String>
    private lateinit var preActivity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initIconAdapter()
        setCategoryIntentData()
        setTextWatcher()
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

    private fun setCategoryIntentData() {
        binding.categoryTitle =
            intent.getStringExtra(CATEGORY_NAME).toString().also {
                originCategoryName = it
            }
        position = intent.getIntExtra("position", 0)
        id = intent.getIntExtra(CATEGORY_ID, 0)
        categoryTitleList = intent.getStringArrayListExtra("categoryNameList") as ArrayList<String>
        preActivity = intent.getStringExtra("preActivity").toString()
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
        binding.ivBack.setOnClickListener { setBackPressedAction() }
    }

    private fun setDeleteTitleBtnClickListener() {
        binding.ivDeleteText.setOnClickListener { deleteCategoryTitle() }
    }

    private fun setTextWatcher() {
        binding.etCategory.addTextChangedListener {
            // ????????? ???????????? ????????? ?????? & ?????? ???????????? ????????? ??????(?????? ???????????? ???????????? ????????? ????????? ??????)
            binding.isDuplicated =
                (binding.categoryTitle in categoryTitleList && binding.categoryTitle != originCategoryName)
        }
    }

    private fun deleteCategoryTitle() = binding.etCategory.text.clear()

    // ???????????? ??? ??????
    private fun setBackPressedAction() {
        if (isModified()) {
            finish()
        } else {
            showBackAlertDialog()
        }
    }

    private fun isModified() =
        (binding.categoryTitle == originCategoryName && clickedPosition == originCategoryImageId)

    // ???????????? ??? ?????? dialog
    private fun showBackAlertDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_CATEGORY, ::finish)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    // ?????? ?????? ?????? ???
    private fun showCategoryDeleteDialog() {
        val dialog = DialogUtil(DialogUtil.REMOVE_CATEGORY, ::requestCategoryDelete)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun setDeleteBtnClickListener() {
        binding.btnRemove.setOnClickListener { showCategoryDeleteDialog() }
    }

    // ?????? ?????? ?????? ???
    private fun setModifyCompleteBtnClickListener() {
        binding.tvComplete.setOnClickListener {
            requestCategoryModify()

        }
    }

    private fun requestCategoryModify() {
        // ????????? ???????????? ?????? ?????? ??????
        categoryViewModel.requestCategoryContent(
            id,
            clickedPosition + 1,
            binding.categoryTitle.toString()
        )
    }

    private fun requestCategoryDelete() {
        // ????????? ?????? ???????????? ?????? ??????
        categoryViewModel.requestCategoryDelete(id)
    }

    private fun sendCategoryModifyResult() {
        // ???????????? ?????? ?????? ?????? ????????? intent
        val orderIntent = Intent(this, CategoryOrderModifyActivity::class.java).apply {
            putExtra("position", position)
            putExtra("categoryName", binding.categoryTitle)
            putExtra("imageId", clickedPosition + 1)
        }
        // ????????? ?????? ????????? intent
        val contentsIntent = Intent(this, ContentsActivity::class.java).apply {
            putExtra("categoryName", binding.categoryTitle)
            putExtra("imageId", clickedPosition + 1)
        }

        when (preActivity) {
            "ContentsActivity" -> setResult(
                RESULT_MODIFY_CATEGORY,
                contentsIntent
            ) // ContentsActivity??? ????????? ??????
            "CategoryOrderModifyActivity" -> setResult(
                RESULT_MODIFY_CATEGORY,
                orderIntent
            ) // CategoryOrderModifyActivity??? ????????? ??????
        }
    }

    private fun sendCategoryDeleteResult() {
        // ???????????? ?????? ?????? ?????? ????????? intent
        val orderIntent = Intent(this, CategoryOrderModifyActivity::class.java).apply {
            putExtra("position", position)
            putExtra("categoryName", binding.etCategory.text)
            putExtra("id", id)
        }
        // ????????? ?????? ????????? intent
        val contentsIntent = Intent(this, ContentsActivity::class.java)

        when (preActivity) {
            "ContentsActivity" -> setResult(
                RESULT_DELETE_CATEGORY,
                contentsIntent
            ) // ContentsActivity??? ????????? ??????
            "CategoryOrderModifyActivity" -> setResult(
                RESULT_DELETE_CATEGORY,
                orderIntent
            ) // CategoryOrderModifyActivity??? ????????? ??????
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
