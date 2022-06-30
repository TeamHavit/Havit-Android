package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.category.CategoryContentModifyActivity.Companion.RESULT_DELETE_CATEGORY
import org.sopt.havit.ui.category.CategoryContentModifyActivity.Companion.RESULT_MODIFY_CATEGORY
import org.sopt.havit.util.DialogUtil

class CategoryOrderModifyActivity :
    BaseBindingActivity<ActivityCategoryOrderModifyBinding>(R.layout.activity_category_order_modify) {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(this) }
    private val originCategoryIdList = mutableListOf<Int>()
    lateinit var holder: RecyclerView.ViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        setResult()
        clickItem()
        setCategoryItemListData()
        clickBack()
        initDrag()
        setCompleteOrder()
    }

    override fun onBackPressed() {
        setBackPressedAction()
    }

    private fun setBackPressedAction() {
        val categoryIdList = mutableListOf<Int>()
        for (item in categoryOrderModifyAdapter.categoryList) {
            categoryIdList.add(item.id)
        }

        // 순서만 같은지 다른지 판별
        if (originCategoryIdList == categoryIdList) {
            finish()
        } else {
            showBackDialog()
        }
    }

    // 뒤로가기 시 뜨는 dialog
    private fun showBackDialog() {
        val dialog = DialogUtil(DialogUtil.CANCEL_EDIT_CATEGORY, ::finish)
        dialog.show(supportFragmentManager, this.javaClass.name)
    }

    private fun initAdapter() {
        categoryOrderModifyAdapter = CategoryOrderModifyAdapter()
        binding.rvContents.adapter = categoryOrderModifyAdapter
    }

    private fun setCategoryItemListData() {
        categoryOrderModifyAdapter.categoryList =
            requireNotNull(intent.getParcelableArrayListExtra("categoryItemList"))
        initOriginCategoryIdList()

        categoryOrderModifyAdapter.notifyDataSetChanged()
    }

    private fun initOriginCategoryIdList() {
        for (item in categoryOrderModifyAdapter.categoryList) {
            originCategoryIdList.add(item.id)
        }
    }

    private fun setResult() {
        // 데이터 받아옴 (카테고리 내용 수정 뷰에서)
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                RESULT_DELETE_CATEGORY -> { // 삭제
                    // 삭제할 카테고리의 정보를 받아옴
                    val position = it.data?.getIntExtra("position", 0) ?: 0

                    // 리사이클러뷰 변경
                    categoryOrderModifyAdapter.removeData(position)
                    // 기존 id 리스트 변경
                    originCategoryIdList.removeAt(position)
                }
                RESULT_MODIFY_CATEGORY -> { // 카테고리 이름 & 아이콘 수정
                    // 수정할 카테고리의 정보를 받아옴
                    val position = it.data?.getIntExtra("position", 0) ?: 0
                    val name = it.data?.getStringExtra("categoryName") ?: "null"
                    val image = it.data?.getIntExtra("imageId", 0) ?: 0

                    // 리사이클러뷰 변경
                    with(categoryOrderModifyAdapter.categoryList[position]) {
                        title = name
                        imageId = image
                        url =
                            "https://havit-bucket.s3.ap-northeast-2.amazonaws.com/category_image/3d_icon_$image.png"
                    }
                    categoryOrderModifyAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    private fun clickItem() {
        categoryOrderModifyAdapter.setItemClickListener(object :
            CategoryOrderModifyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 카테고리 이름 list
                val categoryTitleList = ArrayList<String>()
                for (item in categoryOrderModifyAdapter.categoryList)
                    categoryTitleList.add(item.title)

                val intent = Intent(v.context, CategoryContentModifyActivity::class.java).apply {
                    categoryOrderModifyAdapter.categoryList[position].let {
                        putExtra("categoryId", it.id)
                        putExtra("categoryName", it.title)
                        putExtra("imageId", it.imageId)
                    }
                    putExtra("position", position)
                    putStringArrayListExtra("categoryNameList", categoryTitleList)
                    putExtra("preActivity", "CategoryOrderModifyActivity")
                }

                // 데이터를 담고 전달
                getResult.launch(intent)
            }
        })
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { setBackPressedAction() }
    }

    // drag & drop 코드
    private fun initDrag() {
        val itemTouchCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags =
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.ACTION_STATE_DRAG
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition
                categoryOrderModifyAdapter.swapData(fromPosition, toPosition) // 카테고리 순서 변경
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                categoryOrderModifyAdapter.removeData((viewHolder.layoutPosition))
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                when (actionState) {
                    ItemTouchHelper.ACTION_STATE_DRAG -> { // 순서변경 중
                        holder = viewHolder!!
                        viewHolder.itemView.findViewById<View>(R.id.cl_category_list)
                            .setBackgroundResource(R.drawable.rectangle_purple_light_radius_6)
                    }
                    ItemTouchHelper.ACTION_STATE_IDLE -> { // 순서변경이 끝나면 해당 아이템 레이아웃 변경
                        holder.itemView.findViewById<View>(R.id.cl_category_list)
                            .setBackgroundResource(R.drawable.rectangle_purple_category_radius_6)
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvContents)
    }

    // 완료 시 변경 된 순서 서버에 보내는 함수
    private fun setCompleteOrder() {
        binding.tvComplete.setOnClickListener {
            // id만 담긴 list 추출
            val categoryIdList = mutableListOf<Int>()
            for (item in categoryOrderModifyAdapter.categoryList) {
                categoryIdList.add(item.id)
            }
            // 서버에 순서 변경 요청
            categoryViewModel.requestCategoryOrder(categoryIdList.toList())

            // 서버 post 완료 시 종료
            categoryViewModel.delay.observe(this@CategoryOrderModifyActivity) {
                if (it) {
                    categoryViewModel.setDelay(false)
                    finish()
                }
            }
        }
    }
}
