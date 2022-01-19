package org.sopt.havit.ui.save

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivitySaveBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.mypage.MyPageFragment

class SaveActivity : BaseBindingActivity<ActivitySaveBinding>(R.layout.activity_save) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //val transaction = supportFragmentManager.beginTransaction()
        SaveFragment().show(supportFragmentManager,"save")
        /*transaction.add(R.id.fl_save, fragment)
        transaction.commit()*/

    }
}