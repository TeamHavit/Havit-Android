package org.sopt.havit.ui.category

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.sopt.havit.R
import org.sopt.havit.data.CategoryData
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding

class CategoryOrderModifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryOrderModifyBinding
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    lateinit var holder: RecyclerView.ViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryOrderModifyBinding.inflate(layoutInflater)

        initAdapter()
        setData()
        clickBack()
        initDrag()

        setContentView(binding.root)
    }

    private fun initAdapter() {
        categoryOrderModifyAdapter = CategoryOrderModifyAdapter()
        binding.rvContents.adapter = categoryOrderModifyAdapter
    }

    private fun setData() {
        for (i in 1..15) {
            categoryOrderModifyAdapter.categoryList.add(
                CategoryData(
                    3,
                    "슉슉",
                    "https://user-images.githubusercontent.com/68214704/149118495-e9cc9770-785d-4644-9956-9e17a6641180.png",
                    0,
                    1
                )
            )
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