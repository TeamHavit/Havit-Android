package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.sopt.havit.MainActivity
import org.sopt.havit.R
import org.sopt.havit.data.RetrofitObject
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.util.MySharedPreference

class CategoryOrderModifyActivity : BaseBindingActivity<ActivityCategoryOrderModifyBinding>(R.layout.activity_category_order_modify) {
    private lateinit var getResult: ActivityResultLauncher<Intent>

    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryViewModel: CategoryViewModel by viewModels()
    lateinit var holder: RecyclerView.ViewHolder
    private var SET = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryViewModel
        setContentView(binding.root)

        SERVER = false
        initAdapter()
        setResult()
        clickItem()
        setData()
        clickBack()
        initDrag()
        dataObserve()
        setOrder()
    }

    private fun initAdapter() {
        categoryOrderModifyAdapter = CategoryOrderModifyAdapter()
        binding.rvContents.adapter = categoryOrderModifyAdapter
    }

    private fun setData(){
        SET = intent.getBooleanExtra("dataSet", false)
        if(SET) {
            categoryViewModel.requestCategoryTaken()
            SET = false
        }
    }

    private fun dataObserve() {
        with(categoryViewModel) {
            categoryList.observe(this@CategoryOrderModifyActivity) {
                categoryOrderModifyAdapter.categoryList.addAll(it)
                categoryOrderModifyAdapter.notifyDataSetChanged()

                val list = mutableListOf<Int>()
                for(i in categoryOrderModifyAdapter.categoryList){
                    list.add(i.id)
                }
                Log.d("CategoryOrderList", "변경 전 : ${list}")
            }
        }
    }

    private fun setResult(){
        // 데이터 받아옴 (카테고리 내용 수정 뷰에서)
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val position = it.data?.getIntExtra("position", 0) ?: 0
                val id = it.data?.getIntExtra("id", 0) ?: 0
                categoryViewModel.requestCategoryDelete(id)
                categoryOrderModifyAdapter.removeData(position)
                categoryOrderModifyAdapter.notifyDataSetChanged()
            }
            else if(it.resultCode == RESULT_FIRST_USER){
                val position = it.data?.getIntExtra("position", 0) ?: 0
                val name = it.data?.getStringExtra("categoryName") ?: "null"
                var imageId = it.data?.getIntExtra("imageId", 0) ?: 0
                val id = it.data?.getIntExtra("id", 0) ?: 0
                imageId = imageId+1
                Log.d("CategoryTest", "네임 : $name")
                Log.d("CategoryTest", "아이디 : $id")
                Log.d("CategoryTest", "이미지 : $imageId")
                categoryViewModel.requestCategoryContent(id, imageId, name)
                categoryOrderModifyAdapter.categoryList[position].title = name
                categoryOrderModifyAdapter.categoryList[position].imageId = imageId
                categoryOrderModifyAdapter.categoryList[position].url = "https://havit-bucket.s3.ap-northeast-2.amazonaws.com/category_image/3d_icon_${imageId}.png"

                categoryOrderModifyAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun clickItem(){
        categoryOrderModifyAdapter.setItemClickListener(object : CategoryOrderModifyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val intent = Intent(v.context, CategoryContentModifyActivity::class.java)
                categoryViewModel.categoryList.value?.get(position)
                    ?.let { intent.putExtra("categoryId", categoryOrderModifyAdapter.categoryList[position].id)
                        intent.putExtra("position", position)
                        intent.putExtra("categoryName", categoryOrderModifyAdapter.categoryList[position].title)
                        intent.putExtra("imageId", categoryOrderModifyAdapter.categoryList[position].imageId)}
                Log.d("CategoryContentsData", "전달 전 포지션 : ${position}")
                getResult.launch(intent)
            }
        })
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }

    }

    private fun initDrag() {
        var state = false
        val itemTouchCallback = object : ItemTouchHelper.Callback(
        ) {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                state = true
                holder = viewHolder
                viewHolder.itemView.findViewById<View>(R.id.cl_category_list)
                    .setBackgroundResource(R.drawable.rectangle_purple_light_radius_6)
                val fromPosition: Int = viewHolder.adapterPosition
                val toPosition: Int = target.adapterPosition
                categoryOrderModifyAdapter.swapData(fromPosition, toPosition)
                Log.d("CategoryContentsData", "${viewHolder.layoutPosition}")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                categoryOrderModifyAdapter.removeData((viewHolder.layoutPosition))
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (state) {
                    state = false
                    holder.itemView.findViewById<View>(R.id.cl_category_list)
                        .setBackgroundResource(R.drawable.rectangle_purple_category_radius_6)
                }
            }
        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.rvContents)
    }

    private fun setOrder(){
        binding.tvComplete.setOnClickListener {
            val mlist = mutableListOf<Int>()
            for(i in categoryOrderModifyAdapter.categoryList){
                mlist.add(i.id)
            }
            Log.d("CategoryOrderList", "${mlist.toList()}")

            categoryViewModel.requestCategoryOrder(mlist.toList())

            categoryViewModel.delay.observe(this@CategoryOrderModifyActivity){
                if(it == true){
                    categoryViewModel.setDelay(false)
                    finish()
                }
            }
        }
    }

    companion object{
        var SERVER = false
    }
}