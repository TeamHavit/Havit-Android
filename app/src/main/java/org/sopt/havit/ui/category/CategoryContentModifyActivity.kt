package org.sopt.havit.ui.category

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.contents.ContentsActivity
import org.sopt.havit.ui.share.IconAdapter
import org.sopt.havit.ui.share.IconAdapter.Companion.clickedPosition

class CategoryContentModifyActivity :
    BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private var position = -1
    private var id = -1
    private lateinit var categoryName: String
    private lateinit var iconAdapter: IconAdapter
    private lateinit var categoryTitleList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        setData()
        clickBack()
        deleteText()
        clickDeleteCategory()
        setTextWatcher()
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
        binding.categoryTitle = intent.getStringExtra("categoryName").toString().also { categoryName = it }
        position = intent.getIntExtra("position", 0)
        id = intent.getIntExtra("categoryId", 0)
        categoryTitleList = intent.getStringArrayListExtra("categoryNameList") as ArrayList<String>
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun deleteText(){
        binding.ivDeleteText.setOnClickListener { binding.etCategory.text.clear() }
    }

    private fun setTextWatcher() {
        binding.etCategory.addTextChangedListener {
            // 중복된 카테고리 명인지 검사
            binding.isDuplicated = (binding.categoryTitle in categoryTitleList && binding.categoryTitle != categoryName)
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

            // 만약 카테고리 아이디가 같다면 DELETE 변수를 true로 만들어준다.
            ContentsActivity.isDelete = ContentsActivity.categoryId == id

            finish()
        }

        alertDialog.show()
    }

    private fun clickDeleteCategory() {
        binding.btnRemove.setOnClickListener { setAlertDialog() }
    }

    // 완료 버튼 클릭 시
    private fun clickComplete() {
        binding.tvComplete.setOnClickListener {
            val intent = Intent(this, CategoryOrderModifyActivity::class.java)

            intent.putExtra("position", position)
            intent.putExtra("categoryName", binding.categoryTitle)
            intent.putExtra("imageId", clickedPosition + 1)
            intent.putExtra("id", id)
            setResult(RESULT_FIRST_USER, intent) // 내용 수정에 필요한 데이터

            // 만약 카테고리 아이디가 같다면 카테고리 이름을 변경해준다.
            if(ContentsActivity.categoryId == id) {
                ContentsActivity.categoryName = binding.categoryTitle!!
            }
            finish()
        }
    }
}