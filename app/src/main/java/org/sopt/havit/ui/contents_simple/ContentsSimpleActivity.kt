package org.sopt.havit.ui.contents_simple

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.ActivityContentsSimpleBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.contents.ContentsMoreFragment
import org.sopt.havit.ui.home.HomeFragment
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CONTENT_CHECK_COMPLETE_TYPE
import org.sopt.havit.util.CONTENT_DELETE_TYPE
import org.sopt.havit.util.ToastUtil
import java.io.Serializable

@AndroidEntryPoint
class ContentsSimpleActivity :
    BaseBindingActivity<ActivityContentsSimpleBinding>(R.layout.activity_contents_simple) {

    private val contentsViewModel: ContentsSimpleViewModel by lazy { ContentsSimpleViewModel(this) }
    private lateinit var contentsAdapter: ContentsSimpleRvAdapter
    private lateinit var contentsType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.vmContents = contentsViewModel

        initContents()
        initAdapter()
        decorationView()
        clickBtnBack()
        clickItemView()
        clickItemHavit()
        clickItemMore()
        dataObserve()
    }

    override fun onResume() {
        super.onResume()
        setContents()
    }

    private fun clickItemHavit() {
        contentsAdapter.setHavitClickListener(object :
            ContentsSimpleRvAdapter.OnItemHavitClickListener {
            override fun onHavitClick(v: ImageView, position: Int) {
                with(contentsAdapter) {
                    // 보지 않은 콘텐츠의 경우 콘텐츠 봤다는 토스트 띄움
                    if (!contentsList[position].isSeen)
                        ToastUtil(this@ContentsSimpleActivity).makeToast(
                            CONTENT_CHECK_COMPLETE_TYPE
                        )

                    contentsList[position].isSeen = !contentsList[position].isSeen
                    contentsViewModel.setIsSeen(contentsList[position].id)

                    // tag 바꾸기
                    val isSeen = (v.tag == "seen")
                    v.tag = if (isSeen) "unseen" else "seen"
                    v.setImageResource(if (isSeen) R.drawable.ic_contents_unread else R.drawable.ic_contents_read_2)
                }
            }
        })
    }

    private fun clickItemMore() {
        contentsAdapter.setItemMoreClickListner(object :
            ContentsSimpleRvAdapter.OnItemMoreClickListener {
            override fun onMoreClick(v: View, position: Int) {
                val dataMore = contentsViewModel.contentsList.value?.get(position)?.let {
                    ContentsMoreData(
                        it.id,
                        it.image,
                        it.title,
                        it.createdAt,
                        it.url,
                        it.isNotified,
                        it.notificationTime
                    )
                }

                // 더보기 -> 삭제 클릭 시 수행될 삭제 함수
                val removeItem: (Int) -> Unit = {
                    val list =
                        contentsAdapter.contentsList.toMutableList() // mutable로 해주어야 삭제(수정) 가능
                    list.removeAt(it)
                    // 뷰모델의 콘텐츠 리스트 변수를 업데이트 -> observer를 통해 adapter의 list도 업데이트 된다
                    contentsViewModel.updateContentsList(list)
                    contentsViewModel.decreaseContentsCount(1) // 콘텐츠 개수 1 감소
                    setRemoveToast()
                }

                val bundle = setBundle(dataMore, removeItem, position)
                val dialog = ContentsMoreFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    private fun setRemoveToast() {
        ToastUtil(this).makeToast(CONTENT_DELETE_TYPE)
    }

    // ContentsMoreFragment에 보낼 bundle 생성
    private fun setBundle(
        dataMore: ContentsMoreData?,
        removeItem: (Int) -> Unit,
        position: Int
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ContentsMoreFragment.CONTENTS_MORE_DATA, dataMore)
        bundle.putSerializable(ContentsMoreFragment.REMOVE_ITEM, removeItem as Serializable)
        bundle.putInt(ContentsMoreFragment.POSITION, position)
        return bundle
    }

    private fun clickBtnBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun decorationView() {
        binding.rvContents.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    private fun initAdapter() {
        contentsAdapter = ContentsSimpleRvAdapter()
        binding.rvContents.adapter = contentsAdapter
    }

    private fun initContents() {
        intent?.let { intent ->
            intent.getStringExtra(HomeFragment.CONTENT_TYPE)?.let {
                contentsType = it
            }
        }
    }

    private fun setContents() {
        contentsViewModel.requestContentsTaken(contentsType) // contentsList
        if (contentsType == "unseen") { // topBarName
            contentsViewModel.requestTopBarName(getString(R.string.contents_simple_unseen))
        } else {
            contentsViewModel.requestTopBarName(getString(R.string.contents_simple_recent_save))
        }
    }

    private fun clickItemView() {
        contentsAdapter.setItemClickListener(object :
            ContentsSimpleRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                contentsViewModel.contentsList.value?.get(position)
                    ?.let {
                        intent.putExtra("url", it.url)
                        intent.putExtra("contentsId", it.id)
                        intent.putExtra("isSeen", it.isSeen)
                    }
                startActivity(intent)
            }
        })
    }

    private fun dataObserve() {
        with(contentsViewModel) {
            binding.lifecycleOwner?.let { it ->
                contentsList.observe(it) { data ->
                    if (data.isEmpty()) {
                        if (contentsType == "unseen")
                            requestEmptyContents(getString(R.string.contents_simple_unseen_empty))
                        else
                            requestEmptyContents(getString(R.string.contents_simple_recent_save_empty))
                        binding.tvAddContents.setOnClickListener {
                            SaveFragment("").show(supportFragmentManager, "save")
                        }
                    } else {
                        val min = if (data.size < 20) data.size else 20
                        val list = data.subList(0, min)
                        contentsAdapter.updateList(list)
                    }
                }
                // 로딩중 화면 물러오기
                loadState.observe(it) { state ->
                    // 서버 불러오는 중이라면 스켈레톤 화면 및 shimmer 효과를 보여줌
                    if (state) {
                        binding.sflContents.startShimmer()
                    } else {
                        binding.sflContents.stopShimmer()
                    }
                }
            }
        }
    }
}
