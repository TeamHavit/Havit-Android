package org.sopt.havit.ui.mypage

import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.databinding.ActivityContentsFromMyPageBinding
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.category.CategoryFragment
import org.sopt.havit.ui.contents.ContentsActivity

@AndroidEntryPoint
class ContentsFromMyPageActivity :
    BaseActivity<ActivityContentsFromMyPageBinding>(R.layout.activity_contents_from_my_page) {

    private var pageId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contents_from_my_page)
        pageId = intent.getIntExtra("PageId", -1)
        setFirstPage(pageId)
    }

    private fun setFirstPage(pageId: Int) {
        //SupportFragmentManager로 FragmentManager를 호출
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_view, CategoryFragment()).commit()

        //beginTranscation을 통해 트랜잭션 작업 생성

        val transaction = supportFragmentManager.beginTransaction()
        when (pageId) {
            MY_PAGE_CATEGORY -> {
                transaction.replace(R.id.fragment_container_view, CategoryFragment())
                //작업 수행
                transaction.commit()
            }
            MY_PAGE_SAVED_CONTENTS -> {
                finish()
                startActivity(Intent(this, ContentsActivity::class.java))
            }
            MY_PAGE_SEEN_CONTENTS -> {
                finish()
                startActivity(Intent(this, ContentsActivity::class.java))
            }
        }
    }

    companion object {
        const val MY_PAGE_CATEGORY = 1
        const val MY_PAGE_SAVED_CONTENTS = 2
        const val MY_PAGE_SEEN_CONTENTS = 3
    }

}
