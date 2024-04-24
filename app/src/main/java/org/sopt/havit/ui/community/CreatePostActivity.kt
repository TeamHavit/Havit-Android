package org.sopt.havit.ui.community

import android.os.Bundle
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityCreatePostBinding
import org.sopt.havit.ui.base.BaseActivity

class CreatePostActivity : BaseActivity<ActivityCreatePostBinding>(R.layout.activity_create_post) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
    }
}