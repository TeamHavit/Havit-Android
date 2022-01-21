package org.sopt.havit.ui.category

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.share.ChooseIconFragmentArgs
import org.sopt.havit.ui.share.IconAdapter

class CategoryContentModifyActivity : BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryContentModifyViewModel: CategoryContentModifyViewModel by viewModels()
    var position = -1
    private lateinit var iconAdapter : IconAdapter
    private val args by navArgs<ChooseIconFragmentArgs>()
    private var categoryIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryContentModifyViewModel
        setContentView(binding.root)

        initAdapter()
        setData()
        clickBack()
        clickDelete()
        changeColor()
        clickComplete()
    }

    private fun setData(){
        val name = intent.getStringExtra("categoryName")
        binding.etCategory.setText(name)
        position = intent.getIntExtra("position", 0)
        Log.d("CategoryContentsData", "name : ${name}")
        Log.d("CategoryContentsData", "position : ${position}")
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun changeColor(){
        binding.etCategory.setOnFocusChangeListener { view, b -> binding.etCategory.setTextColor(Color.parseColor("#272b30")) }
    }

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
            val intentName = Intent(this, CategoryOrderModifyActivity::class.java).apply {
                putExtra("position", position)
                putExtra("categoryName", binding.etCategory.text)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        alertDialog.show()
    }

    private fun clickDelete() {
        binding.btnRemove.setOnClickListener { setAlertDialog() }
    }

    private fun clickComplete(){
        binding.tvComplete.setOnClickListener {
            val intentName = Intent(this, CategoryOrderModifyActivity::class.java)
            intentName.putExtra("position2", position)
            intentName.putExtra("categoryName", binding.etCategory.text.toString())
            Log.d("CategoryNameTest", "전달 전 : ${binding.etCategory.text}")
            setResult(RESULT_FIRST_USER, intentName)
            finish()
        }
    }

    private fun initAdapter() {

        iconAdapter = IconAdapter()
        binding.rvIcon.adapter = iconAdapter
        iconAdapter.iconList.addAll(
            listOf(
                R.drawable.ic_category1,
                R.drawable.ic_category2,
                R.drawable.ic_category3,
                R.drawable.ic_category4,
                R.drawable.ic_category5,
                R.drawable.ic_category6,
                R.drawable.ic_category7,
                R.drawable.ic_category8,
                R.drawable.ic_category9,
                R.drawable.ic_category10,
                R.drawable.ic_category11,
                R.drawable.ic_category12,
                R.drawable.ic_category13,
                R.drawable.ic_category14,
                R.drawable.ic_category15
            )
        )
        iconAdapter.notifyDataSetChanged()


        iconAdapter.setItemClickListener(object : IconAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.d("IconAdapter", "$position clicked in Fragment")
                v.background =
                    ContextCompat.getDrawable(v.context, R.drawable.oval_gray_stroke_2)
                categoryIndex = position + 1
            }
        })
    }
}