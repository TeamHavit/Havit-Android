package org.sopt.havit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.sopt.havit.databinding.ActivityShareBinding

class ShareActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_share, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetDialog.show()
    }
}