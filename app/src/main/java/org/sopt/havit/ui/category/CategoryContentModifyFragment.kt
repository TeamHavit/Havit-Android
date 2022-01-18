package org.sopt.havit.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.sopt.havit.R
import org.sopt.havit.databinding.FragmentCategoryContentModifyBinding

class CategoryContentModifyFragment : Fragment() {
    private var _binding: FragmentCategoryContentModifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryContentModifyBinding.inflate(inflater, container, false)

        clickBack()
        clickDelete()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickBack(){
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setAlertDialog(){
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_category_delete, null)

        val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialogStyle)
            .setView(view)
            .create()

        val buttonClose =  view.findViewById<View>(R.id.btn_cancel)
        val buttonDelete = view.findViewById<View>(R.id.btn_delete)
        buttonClose.setOnClickListener {
            alertDialog.dismiss()
        }
        buttonDelete.setOnClickListener{

        }

        alertDialog.show()
    }

    private fun clickDelete(){
        binding.btnRemove.setOnClickListener { setAlertDialog() }
    }
}