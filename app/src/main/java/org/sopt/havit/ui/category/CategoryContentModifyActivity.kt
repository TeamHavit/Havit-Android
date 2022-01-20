package org.sopt.havit.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCategoryContentModifyBinding
import org.sopt.havit.databinding.ActivityCategoryOrderModifyBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class CategoryContentModifyActivity : BaseBindingActivity<ActivityCategoryContentModifyBinding>(R.layout.activity_category_content_modify) {
    private lateinit var categoryOrderModifyAdapter: CategoryOrderModifyAdapter
    private val categoryContentModifyViewModel: CategoryContentModifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.categoryViewModel = categoryContentModifyViewModel
        setContentView(binding.root)

        clickBack()
        clickDelete()

    }

    private fun setData(){
        val name = intent.getStringExtra("categoryName")
        binding.etCategory.setText(name)
    }

    private fun clickBack() {
        binding.ivBack.setOnClickListener { finish() }
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

        }

        alertDialog.show()
    }

    private fun clickDelete() {
        binding.btnRemove.setOnClickListener { setAlertDialog() }
    }
}