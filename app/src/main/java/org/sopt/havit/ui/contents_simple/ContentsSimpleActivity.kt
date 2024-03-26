package org.sopt.havit.ui.contents_simple

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.databinding.ActivityContentsSimpleBinding
import org.sopt.havit.domain.entity.NetworkState
import org.sopt.havit.ui.base.BaseActivity
import org.sopt.havit.ui.contents.more.ContentsMoreFragment
import org.sopt.havit.ui.home.HomeFragment
import org.sopt.havit.ui.save.SaveFragment
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CONTENT_CHECK_COMPLETE_TYPE
import org.sopt.havit.util.CONTENT_DELETE_TYPE
import org.sopt.havit.util.CustomDecoration
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.ToastUtil
import org.sopt.havit.util.setOnSingleClickListener
import java.io.Serializable

@AndroidEntryPoint
class ContentsSimpleActivity :
    BaseActivity<ActivityContentsSimpleBinding>(R.layout.activity_contents_simple) {

    private val contentsViewModel: ContentsSimpleViewModel by viewModels()
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
        clickRefreshData()
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

                val showDeleteDialog: () -> Unit = {
                    val dialog =
                        DialogUtil(DialogUtil.REMOVE_CONTENTS) {
                            removeItem(
                                position,
                                requireNotNull(dataMore?.id)
                            )
                        }
                    dialog.show(supportFragmentManager, this.javaClass.name)
                }
                val requestContentsData = ::setContents as Serializable
                val bundle = setBundle(dataMore, showDeleteDialog, requestContentsData, position)
                val dialog = ContentsMoreFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    private fun removeItem(pos: Int, contentsId: Int) {
        val list =
            contentsAdapter.contentsList.toMutableList() // mutable로 해주어야 삭제(수정) 가능
        list.removeAt(pos)
        // 뷰모델의 콘텐츠 리스트 변수를 업데이트 -> observer를 통해 adapter의 list도 업데이트 된다
        contentsViewModel.updateContentsList(list)
        contentsViewModel.decreaseContentsCount(1) // 콘텐츠 개수 1 감소

        // 서버 요청
        contentsViewModel.deleteContents(contentsId)
        setRemoveToast()
    }

    private fun setRemoveToast() {
        ToastUtil(this).makeToast(CONTENT_DELETE_TYPE)
    }

    // ContentsMoreFragment에 보낼 bundle 생성
    private fun setBundle(
        dataMore: ContentsMoreData?,
        showDeleteDialog: () -> Unit,
        refreshData: Serializable,
        position: Int
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ContentsMoreFragment.CONTENTS_MORE_DATA, dataMore)
        bundle.putSerializable(
            ContentsMoreFragment.SHOW_DELETE_DIALOG,
            showDeleteDialog as Serializable
        )
        bundle.putSerializable(
            ContentsMoreFragment.REFRESH_DATA,
            refreshData
        )
        bundle.putInt(ContentsMoreFragment.POSITION, position)
        return bundle
    }

    private fun clickBtnBack() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    // 서버 연결 오류 시 새로 고침(서버 재호출)
    private fun clickRefreshData() {
        binding.layoutNetworkError.ivRefresh.setOnSingleClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotation_refresh))
            setContents()
        }
    }

    private fun decorationView() {
        // 1dp를 float형으로 변환
        val oneDp: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1f,
            this.resources.displayMetrics
        )

        // divider 색 변경을 위한 CustomItemDecoration
        binding.rvContents.addItemDecoration(
            CustomDecoration(
                oneDp,
                oneDp,
                Color.parseColor("#f3f3f3")
            )
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
                // 로딩중 화면 불러오기
                loadState.observe(it) { state ->
                    // 서버 불러오는 중이라면 스켈레톤 화면 및 shimmer 효과를 보여줌
                    if (state == NetworkState.LOADING) {
                        binding.sflContents.startShimmer()
                    } else {
                        binding.sflContents.stopShimmer()
                    }
                }
            }
        }
    }
}
