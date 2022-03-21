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


class CategoryOrderModifyActivity :
    BaseBindingActivity<ActivityCategoryOrderModifyBinding>(R.layout.activity_category_order_modify) {
    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryViewModel: CategoryViewModel by lazy { CategoryViewModel(this) }
    lateinit var holder: RecyclerView.ViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryViewModel
        setContentView(binding.root)

        initAdapter()
        setResult()
        clickItem()
        requestServer()
        clickBack()
        initDrag()
        dataObserve()
        setCompleteOrder()
    }

    private fun initAdapter() {
        categoryOrderModifyAdapter = CategoryOrderModifyAdapter()
        binding.rvContents.adapter = categoryOrderModifyAdapter
    }

    private fun requestServer() {
        categoryViewModel.requestCategoryTaken()
    }

    private fun dataObserve() {
        with(categoryViewModel) {
            categoryList.observe(this@CategoryOrderModifyActivity) {
                categoryOrderModifyAdapter.categoryList.addAll(it)
                categoryOrderModifyAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setResult() {
        // 데이터 받아옴 (카테고리 내용 수정 뷰에서)
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when(it.resultCode){
                RESULT_OK -> { // 삭제
                    // 삭제할 카테고리의 정보를 받아옴
                    val position = it.data?.getIntExtra("position", 0) ?: 0
                    val id = it.data?.getIntExtra("id", 0) ?: 0
                    // 서버에 삭제 요청
                    categoryViewModel.requestCategoryDelete(id)
                    // 리사이클러뷰 변경
                    categoryOrderModifyAdapter.removeData(position)
                }
                RESULT_FIRST_USER -> { // 카테고리 이름 & 아이콘 수정
                    // 수정할 카테고리의 정보를 받아옴
                    val position = it.data?.getIntExtra("position", 0) ?: 0
                    val name = it.data?.getStringExtra("categoryName") ?: "null"
                    var image = it.data?.getIntExtra("imageId", 0) ?: 0
                    val id = it.data?.getIntExtra("id", 0) ?: 0

                    // 서버에 수정된 내용 전달
                    categoryViewModel.requestCategoryContent(id, image, name)
                    // 리사이클러뷰 변경
                    with(categoryOrderModifyAdapter.categoryList[position]){
                        title = name
                        imageId = image
                        url = "https://havit-bucket.s3.ap-northeast-2.amazonaws.com/category_image/3d_icon_${image}.png"
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
                val intent = Intent(v.context, CategoryContentModifyActivity::class.java)

                categoryOrderModifyAdapter.categoryList[position].let {
                    intent.putExtra("categoryId", it.id)
                    intent.putExtra("categoryName", it.title)
                    intent.putExtra("imageId", it.imageId)
                }
                intent.putExtra("position", position)
                // 카테고리 이름 list
                val list = ArrayList<String>()
                for (item in categoryOrderModifyAdapter.categoryList)
                    list.add(item.title)
                intent.putStringArrayListExtra("categoryNameList", list)

                // 데이터를 담고 전달
                getResult.launch(intent)
            }
        })
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }
    }

    // drag & drop 코드
    private fun initDrag() {
        val itemTouchCallback = object : ItemTouchHelper.Callback(
        ) {
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
                when(actionState){
                    ItemTouchHelper.ACTION_STATE_DRAG -> { // 순서변경 중
                        holder = viewHolder!!
                        viewHolder.itemView.findViewById<View>(R.id.cl_category_list)
                            .setBackgroundResource(R.drawable.rectangle_purple_light_radius_6)
                    }
                    ItemTouchHelper.ACTION_STATE_IDLE->{ // 순서변경이 끝나면 해당 아이템 레이아웃 변경
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
            val idList = mutableListOf<Int>()
            for (item in categoryOrderModifyAdapter.categoryList) {
                idList.add(item.id)
            }
            // 서버에 순서 변경 요청
            categoryViewModel.requestCategoryOrder(idList.toList())

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