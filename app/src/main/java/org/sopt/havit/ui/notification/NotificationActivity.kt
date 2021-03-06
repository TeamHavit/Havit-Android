package org.sopt.havit.ui.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.sopt.havit.R
import org.sopt.havit.data.remote.ContentsMoreData
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.databinding.ActivityNotificationBinding
import org.sopt.havit.ui.base.BaseBindingActivity
import org.sopt.havit.ui.contents.more.ContentsMoreFragment
import org.sopt.havit.ui.web.WebActivity
import org.sopt.havit.util.CONTENT_CHECK_COMPLETE_TYPE
import org.sopt.havit.util.CONTENT_DELETE_TYPE
import org.sopt.havit.util.DialogUtil
import org.sopt.havit.util.ToastUtil
import java.io.Serializable

@AndroidEntryPoint
class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
    private val notificationViewModel: NotificationViewModel by lazy { NotificationViewModel(this) }
    private lateinit var notificationAdapter: NotificationRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        option = before
        initAdapter()
        initListener()
        decorationView()
    }

    override fun onStart() {
        super.onStart()
        setData()
        dataObserve()
    }

    private fun initAdapter() {
        notificationAdapter = NotificationRvAdapter()
        binding.rvNotification.adapter = notificationAdapter
    }

    private fun initListener() {
        clickChip()
        clickBackBtn()
        clickItemMore()
        clickItemHavit()
        clickItemView()
    }

    private fun decorationView() {
        binding.rvNotification.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    // ????????????/???????????? ??? ?????? ?????????
    private fun clickChip() {
        binding.cgStatus.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "clickChip: $checkedId")
            when (checkedId) {
                binding.chipWillAlarm.id -> option = before
                binding.chipDoneAlarm.id -> option = after
            }
            setData()
        }
    }

    // ???????????? ?????? ?????? ?????????
    private fun clickBackBtn() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun clickItemMore() {
        notificationAdapter.setItemMoreClickListner(object :
            NotificationRvAdapter.OnItemMoreClickListener {
            override fun onMoreClick(v: View, position: Int) {
                val dataMore = notificationViewModel.contentsList.value?.get(position)?.let {
                    ContentsMoreData(
                        it.id,
                        it.image,
                        it.title,
                        it.createdAt,
                        it.url,
                        true,
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

                val bundle = setBundle(dataMore, showDeleteDialog, position)
                val dialog = ContentsMoreFragment()
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "setting")
            }
        })
    }

    private fun removeItem(pos: Int, contentsId: Int) {
        val list =
            notificationAdapter.contentsList.toMutableList() // mutable??? ???????????? ??????(??????) ??????
        list.removeAt(pos)
        // ???????????? ????????? ????????? ????????? ???????????? -> observer??? ?????? adapter??? list??? ???????????? ??????
        notificationViewModel.updateContentsList(list)
        setRemoveToast()

        // ?????? ??????
        notificationViewModel.deleteContents(contentsId)
        setRemoveToast()
    }

    // ContentsMoreFragment??? ?????? bundle ??????
    private fun setBundle(
        dataMore: ContentsMoreData?,
        showDeleteDialog: () -> Unit,
        position: Int
    ): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ContentsMoreFragment.CONTENTS_MORE_DATA, dataMore)
        bundle.putSerializable(
            ContentsMoreFragment.SHOW_DELETE_DIALOG,
            showDeleteDialog as Serializable
        )
        bundle.putInt(ContentsMoreFragment.POSITION, position)
        return bundle
    }

    private fun clickItemHavit() {
        notificationAdapter.setHavitClickListener(object :
            NotificationRvAdapter.OnItemHavitClickListener {
            override fun onHavitClick(v: ImageView, position: Int) {
                with(notificationAdapter) {
                    // ?????? ?????? ???????????? ?????? ????????? ????????? ????????? ??????
                    var isSeen = contentsList[position].isSeen
                    if (!isSeen) {
                        setHavitToast()
                    }

                    isSeen = !isSeen
                    contentsList[position].isSeen = isSeen
                    notificationViewModel.setIsSeen(contentsList[position].id)
                    v.setImageResource(if (isSeen) R.drawable.ic_contents_read_2 else R.drawable.ic_contents_unread)
                }
            }
        })
    }

    // ????????? ?????? ?????? ?????????
    private fun setHavitToast() {
        ToastUtil(this).makeToast(CONTENT_CHECK_COMPLETE_TYPE)
    }

    // ????????? ?????? ?????????
    private fun setRemoveToast() {
        ToastUtil(this).makeToast(CONTENT_DELETE_TYPE)
    }

    private fun clickItemView() {
        notificationAdapter.setItemClickListener(object :
            NotificationRvAdapter.OnItemClickListener {
            override fun onWebClick(v: View, position: Int) {
                val intent = Intent(v.context, WebActivity::class.java)
                notificationViewModel.contentsList.value?.get(position)?.let {
                    intent.putExtra("url", it.url)
                    intent.putExtra("contentsId", it.id)
                    intent.putExtra("isSeen", it.isSeen)
                }
                startActivity(intent)
            }
        })
    }

    private fun setData() {
        binding.option = option
        notificationViewModel.requestContentsTaken(option)
    }

    private fun dataObserve() {
        with(notificationViewModel) {
//            // ?????? ?????? Empty??? ?????????
//            contentLoadState.observe(this@NotificationActivity) { isLoading ->
//                if (isLoading) {
//                    binding.rvNotification.visibility = View.GONE
//                    binding.clAlarmEmpty.visibility = View.GONE
//                }
//            }
            // ????????? ????????????
            contentsList.observe(this@NotificationActivity) { data ->
                setContent(data)
            }
        }
    }

    private fun setContent(data: List<NotificationResponse.NotificationData>) {
        if (data.isEmpty()) {
            binding.isEmpty = true
        } else {
            binding.isEmpty = false
            notificationAdapter.updateList(data)
        }
    }

    companion object {
        const val TAG = "NOTIFICATION_ACTIVITY"
        const val before = "before"
        const val after = "after"
        var option = before
    }
}
