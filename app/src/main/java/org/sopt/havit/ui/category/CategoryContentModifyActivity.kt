package org.sopt.havit.ui.category

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.share.IconAdapter
import org.sopt.havit.ui.share.IconAdapter.Companion.clickedPosition

class CategoryContentModifyActivity :
    BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private val categoryContentModifyViewModel: CategoryContentModifyViewModel by lazy {
        CategoryContentModifyViewModel(
            this
        )
    }
    var position = -1
    var id = -1
    private lateinit var iconAdapter: IconAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryContentModifyViewModel
        setContentView(binding.root)

        initAdapter()
        setData()
        clickBack()
        clickDelete()
        changeTextColor()
        clickComplete()
    }

    private fun initAdapter() {
        clickedPosition = intent.getIntExtra("imageId", 0) - 1
        binding.rvIcon.adapter = IconAdapter(::onIconClick).also { iconAdapter = it }
    }

    private fun onIconClick(position: Int) {
        val previousPosition = clickedPosition
        clickedPosition = position
        iconAdapter.notifyItemChanged(previousPosition)
        iconAdapter.notifyItemChanged(clickedPosition)
    }

    private fun setData() {
        val name = intent.getStringExtra("categoryName")
        binding.etCategory.setText(name)
        position = intent.getIntExtra("position", 0)
        id = intent.getIntExtra("categoryId", 0)
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun changeTextColor() {
        // 포커그사 있을 시 색 바꿈
        binding.etCategory.setOnFocusChangeListener { view, b ->
            binding.etCategory.setTextColor(
                Color.parseColor("#272b30")
            )
        }
    }

    // dialog util이 만들어지면 수정될 코드입니당
    private fun setAlertDialog() {
        val layoutInflater = LayoutInflater.from(this)
        val view = layoutInflater.inflate(R.layout.dialog_category_delete, null)

        val alertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
            .setView(view)
            .create()

        val buttonClose = view.findViewById<View>(R.id.btn_cancel)
        val buttonDelete = view.findViewById<View>(R.id.btn_delete)

        buttonClose.setOnClickListener {
            alertDialog.dismiss()
        }
        buttonDelete.setOnClickListener {
            // 관리 뷰에 전달할 데이터 셋팅
            val intent = Intent(this, CategoryOrderModifyActivity::class.java).apply {
                putExtra("position", position)
                putExtra("categoryName", binding.etCategory.text)
                putExtra("id", id)
            }
            setResult(RESULT_OK, intent) // 삭제에 필요한 데이터
            alertDialog.dismiss() // window leak 방지
            finish()
        }

        alertDialog.show()
    }

    private fun clickDelete() {
        binding.btnRemove.setOnClickListener { setAlertDialog() }
    }

    // 완료 버튼 클릭 시
    private fun clickComplete() {
        binding.tvComplete.setOnClickListener {
            val intentName = Intent(this, CategoryOrderModifyActivity::class.java)
            intentName.putExtra("position", position)
            intentName.putExtra("categoryName", binding.etCategory.text.toString())
            intentName.putExtra("imageId", clickedPosition)
            intentName.putExtra("id", id)
            setResult(RESULT_FIRST_USER, intentName) // 내용 수정에 필요한 데이터
            finish()
        }
    }
}