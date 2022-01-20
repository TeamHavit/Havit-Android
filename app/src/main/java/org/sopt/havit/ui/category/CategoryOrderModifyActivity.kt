package org.sopt.havit.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class CategoryOrderModifyActivity : BaseBindingActivity<ActivityCategoryOrderModifyBinding>(R.layout.activity_category_order_modify) {
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryViewModel: CategoryViewModel by viewModels()
    lateinit var holder: RecyclerView.ViewHolder
    private var SET = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryViewModel
        setContentView(binding.root)

        initAdapter()
        setData()
        clickBack()
        initDrag()
        dataObserve()

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
            }
        }
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
}