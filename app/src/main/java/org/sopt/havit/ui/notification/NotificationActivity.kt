package org.sopt.havit.ui.notification

import android.os.Bundle
import android.util.Log
import org.sopt.havit.R
import org.sopt.havit.data.remote.NotificationResponse
import org.sopt.havit.databinding.ActivityNotificationBinding
import org.sopt.havit.ui.base.BaseBindingActivity

class NotificationActivity :
    BaseBindingActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
    private val notificationViewModel: NotificationViewModel by lazy { NotificationViewModel(this) }
    private lateinit var notificationAdapter: NotificationRvAdapter
    private var option = "before"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        initListener()
        clickBackBtn()
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
    }

    // 알림예정/지난알림 칩 클릭 이벤트
    private fun clickChip() {
        binding.cgStatus.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "clickChip: $checkedId")
            when (checkedId) {
                binding.chipWillAlarm.id -> option = "before"
                binding.chipDoneAlarm.id -> option = "after"
            }
            Log.d(TAG, "clickChip: option : $option")
            setData()
        }
    }

    // 뒤로가기 버튼 클릭 이벤트
    private fun clickBackBtn() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setData() {
        notificationViewModel.requestContentsTaken(option)
    }

    private fun dataObserve() {
        with(notificationViewModel) {
            binding.lifecycleOwner?.let { it ->
                contentsList.observe(it) { data ->
                    setContent(data)
                }
            }
        }
    }

    private fun setContent(data: List<NotificationResponse.NotificationData>) {
        if (data.isEmpty()) {

        } else {
            notificationAdapter.contentsList.clear()
            notificationAdapter.contentsList.addAll(data)
            notificationAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val TAG = "NOTIFICATION_ACTIVITY"
    }
}